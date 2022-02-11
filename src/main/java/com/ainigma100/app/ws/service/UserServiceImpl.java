package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.AddressDTO;
import com.ainigma100.app.ws.dto.UserDTO;
import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.exception.RecordAlreadyExistsException;
import com.ainigma100.app.ws.exception.RecordNotFoundException;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import com.ainigma100.app.ws.repository.UserRepository;
import com.ainigma100.app.ws.utils.SortItem;
import com.ainigma100.app.ws.utils.Utils;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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
    public UserDTO createUser(UserDTO userDto) {

        UserEntity userFromDb = userRepository.findByEmail(userDto.getEmail());

        if (userFromDb != null) {
            throw new RecordAlreadyExistsException("User with email '" + userFromDb.getEmail() + "' already exists");
        }

        for (int i = 0; i < userDto.getAddresses().size(); i++) {

            AddressDTO addressDTO = userDto.getAddresses().get(i);
            addressDTO.setUserDetails(userDto);
            addressDTO.setAddressId(utils.generateRandomId());

            // set back the updated address to the userDto
            userDto.getAddresses().set(i, addressDTO);
        }


        UserEntity userEntity = utils.map(userDto, UserEntity.class);

        userEntity.setUserId(utils.generateRandomId());
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
    public UserDTO getUserByUserId(String userId) {

        UserEntity userFromDb = userRepository.findByUserId(userId);

        if (userFromDb == null) {
            throw new RecordNotFoundException("Record with userId: '" + userId + "' was not found!");
        }

        // map the object into the preferred return type
        UserDTO returnValue = utils.map(userFromDb, UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDto) {

        UserEntity userFromDb = userRepository.findByUserId(userId);

        if (userFromDb == null) {
            throw new RecordNotFoundException("Record with userId: '" + userId + "' was not found!");
        }

        userFromDb.setFirstName(userDto.getFirstName());
        userFromDb.setLastName(userDto.getLastName());

        UserEntity updatedUser = userRepository.save(userFromDb);

        // map the object into the preferred return type
        UserDTO returnValue = utils.map(updatedUser, UserDTO.class);

        return returnValue;
    }

    @Override
    public ResponseEntity<String> deleteUserByUserId(String userId) {

        UserEntity userFromDb = userRepository.findByUserId(userId);

        if (userFromDb == null) {
            throw new RecordNotFoundException("Record with userId: '" + userId + "' was not found!");
        }

        userRepository.delete(userFromDb);

        String returnValue = "Record with userId: '"  + userId + "' has been deleted!";
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
            // provided by Spring
            throw new UsernameNotFoundException(email);
        }

        // User is provided by Spring
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}