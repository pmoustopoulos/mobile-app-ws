package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.AddressDTO;
import com.ainigma100.app.ws.dto.UserDTO;
import com.ainigma100.app.ws.entity.PasswordResetTokenEntity;
import com.ainigma100.app.ws.entity.RoleEntity;
import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.exception.RecordAlreadyExistsException;
import com.ainigma100.app.ws.exception.RecordNotFoundException;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import com.ainigma100.app.ws.repository.PasswordResetTokenRepository;
import com.ainigma100.app.ws.repository.RoleRepository;
import com.ainigma100.app.ws.repository.UserRepository;
import com.ainigma100.app.ws.security.UserPrincipal;
import com.ainigma100.app.ws.utils.SortItem;
import com.ainigma100.app.ws.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    CellStyle headerCellStyle = null;
    CellStyle formatTextCellStyle = null;
    CellStyle formatDateCellStyle = null;
    CellStyle formatDateTimeCellStyle = null;
    CellStyle formatNumberCellStyle = null;


    @Override
    public ResponseEntity<Page<UserDetailsResponseModel>> getUsersUsingPagination(UserSearchCriteria userSearchCriteria) {

        Integer page = userSearchCriteria.getPage();
        Integer size = userSearchCriteria.getSize();
        List<SortItem> sortList = userSearchCriteria.getSortList();


        // this pageable will be used for the pagination.
        Pageable pageable = utils.createPageableBasedOnPageAndSizeAndSorting(sortList, page, size);

        // get records from the database
        Page<UserEntity> userEntityPageFromDb = userRepository.getUsersUsingPagination(userSearchCriteria, pageable);

        // map the object into the preferred return type
        Page<UserDetailsResponseModel> returnValue = utils.mapPage(userEntityPageFromDb, UserDetailsResponseModel.class);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @Override
    public boolean requestPasswordReset(String email, HttpServletResponse response) {

        boolean returnValue = false;

        UserEntity userFromDb = userRepository.findByEmail(email);

        if (userFromDb == null) {
            return returnValue;
        }

        // generate a password reset token
        String token = utils.generatePasswordResetToken(userFromDb.getId());

        // store token and associate it to a specific user
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userFromDb);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

        // send email to the user with the reset token
        log.info("Reset token {}", token);
        // add the information to the header just for testing
        response.addHeader("ResetPassToken", token);

        return true;

    }

    @Override
    public boolean resetPassword(String token, String newPassword) {

        boolean returnValue = false;

        if (utils.isTokenExpired(token)) {
            return returnValue;
        }

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        // prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

        // Update UserEntity password in the database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        // Verify that password was saved successfully
        if (updatedUserEntity != null && updatedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }

        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return returnValue;
    }

    @Override
    public ByteArrayInputStream getExcelReport() {

        List<UserEntity> userEntityList = userRepository.findAll();

        String[] userEntityColumns = {
                "USER ID", "FIRST NAME", "LAST NAME", "EMAIL",
                "CREATION DATE", "MODIFICATION DATE"
        };

        try (Workbook workbook = new XSSFWorkbook()) {

            // create excel sheet
            Sheet userEntitySheet = workbook.createSheet("Users");

            // prepare Header Cell Style
            this.prepareHeaderCellStyle(workbook);

            // create Header Row
            Row userEntityHeaderRow = userEntitySheet.createRow(0);

            // Write Header
            for (int col = 0; col < userEntityColumns.length; col++) {
                Cell cell = userEntityHeaderRow.createCell(col);
                cell.setCellValue(userEntityColumns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // prepare Cell Style
            this.prepareCellStyle(workbook);

            // we start from one because we have already set the headers to zero
            int rowIndex = 1;
            for (UserEntity userRecord : userEntityList) {

                Row row = userEntitySheet.createRow(rowIndex++);

                // set each column value inside the Excel
                Utils.setCellValue(userRecord.getId(), row, 0, formatTextCellStyle);
                Utils.setCellValue(userRecord.getFirstName(), row, 1, formatTextCellStyle);
                Utils.setCellValue(userRecord.getLastName(), row, 2, formatTextCellStyle);
                Utils.setCellValue(userRecord.getEmail(), row, 3, formatTextCellStyle);
                Utils.setCellValue(userRecord.getCreatedAt(), row, 4, formatDateTimeCellStyle);
                Utils.setCellValue(userRecord.getUpdatedAt(), row, 5, formatDateTimeCellStyle);

            }


            // format column size
            for (int col = 0; col < userEntityColumns.length; col++) {
                userEntitySheet.autoSizeColumn(col);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }


    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hasTokenExpired = utils.isTokenExpired(token);
            if (!hasTokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

        return returnValue;
    }


    @Override
    public UserDTO createUser(UserDTO userDto, HttpServletResponse response) {

        UserEntity userFromDb = userRepository.findByEmail(userDto.getEmail());

        if (userFromDb != null) {
            throw new RecordAlreadyExistsException("User with email '" + userFromDb.getEmail() + "' already exists");
        }

        for (int i = 0; i < userDto.getAddresses().size(); i++) {

            AddressDTO addressDTO = userDto.getAddresses().get(i);
            addressDTO.setUserDetails(userDto);

            // set back the updated address to the userDto
            userDto.getAddresses().set(i, addressDTO);
        }


        UserEntity userEntity = utils.map(userDto, UserEntity.class);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(userEntity.getId()));

        // Get the roles from the UserDTO
        Collection<RoleEntity> roleEntities = new HashSet<>();
        for(String role: userDto.getRoles()) {

            RoleEntity roleEntity = roleRepository.findByName(role);

            if(roleEntity != null) {
                roleEntities.add(roleEntity);
            }
        }
        // set the roles to UserEntity
        userEntity.setRoles(roleEntities);

        UserEntity savedUser = userRepository.save(userEntity);

        // Send an email message to user to verify their email address
        log.warn("Email verification token is {}", savedUser.getEmailVerificationToken());
        // add the information to the header just for testing
        response.addHeader("EmailVerificationToken", savedUser.getEmailVerificationToken());

        // map the object into the preferred return type
        UserDTO returnValue = utils.map(savedUser, UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDTO getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException(email);		// provided by Spring
        }

        // map the object into the preferred return type
        UserDTO returnValue = utils.map(userEntity, UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDTO getUserById(String userId) {

        Optional<UserEntity> userFromDb = userRepository.findById(userId);

        if (userFromDb.isEmpty()) {
            throw new RecordNotFoundException("Record with id: '" + userId + "' was not found!");
        }

        // map the object into the preferred return type
        UserDTO returnValue = utils.map(userFromDb.get(), UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDto) {

        Optional<UserEntity> userFromDb = userRepository.findById(userId);

        if (userFromDb.isEmpty()) {
            throw new RecordNotFoundException("Record with id: '" + userId + "' was not found!");
        }

        userFromDb.get().setFirstName(userDto.getFirstName());
        userFromDb.get().setLastName(userDto.getLastName());

        UserEntity updatedUser = userRepository.save(userFromDb.get());

        // map the object into the preferred return type
        UserDTO returnValue = utils.map(updatedUser, UserDTO.class);

        return returnValue;
    }

    @Override
    public ResponseEntity<String> deleteUserById(String userId) {

        Optional<UserEntity> userFromDb = userRepository.findById(userId);

        if (userFromDb.isEmpty()) {
            throw new RecordNotFoundException("Record with id: '" + userId + "' was not found!");
        }

        userRepository.delete(userFromDb.get());

        String returnValue = "Record with id: '"  + userId + "' has been deleted!";
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }


    /**
     * This method is used to load user details from the database by username (in our case the username is the email)
     * It will be used by Spring Framework to load User details. Furthermore, it is used to sign in our user
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) {
            log.error("User {} not found in the database", email);
            // provided by Spring
            throw new UsernameNotFoundException(email);
        }

        log.info("User {} found in the database", email);

        // User is provided by Spring
//        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());

        return new UserPrincipal(userEntity);

    }


    /**
     * This method is used to prepare the Header Cell Style
     * @param workbook
     */
    private void prepareHeaderCellStyle(Workbook workbook) {

        // Header Font
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        // Header Cell Style
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }


    /**
     * This method is used to prepare the Cell Style
     * @param workbook
     */
    private void prepareCellStyle(Workbook workbook) {
        // format text cell
        formatTextCellStyle = workbook.createCellStyle();
        formatTextCellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));

        // format date cells
        formatDateCellStyle = workbook.createCellStyle();
        formatDateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd-mm-yyyy"));

        // format date time cells
        formatDateTimeCellStyle = workbook.createCellStyle();
        formatDateTimeCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd-mm-yyyy HH:mm:ss"));

        // format number cells
        formatNumberCellStyle = workbook.createCellStyle();
        formatNumberCellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
    }

}
