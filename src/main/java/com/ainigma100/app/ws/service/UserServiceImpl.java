package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.AddressDTO;
import com.ainigma100.app.ws.dto.UserDTO;
import com.ainigma100.app.ws.entity.PasswordResetTokenEntity;
import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.exception.RecordAlreadyExistsException;
import com.ainigma100.app.ws.exception.RecordNotFoundException;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import com.ainigma100.app.ws.repository.PasswordResetTokenRepository;
import com.ainigma100.app.ws.repository.UserRepository;
import com.ainigma100.app.ws.utils.SortItem;
import com.ainigma100.app.ws.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


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
    public boolean requestPasswordReset(String email) {

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
    public UserDTO createUser(UserDTO userDto) {

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

        UserEntity savedUser = userRepository.save(userEntity);

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
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
