package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.UserDto;
import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.exception.RecordAlreadyExistsException;
import com.ainigma100.app.ws.repository.UserRepository;
import com.ainigma100.app.ws.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto createUser(UserDto userDto) {

        UserEntity userFromDb = userRepository.findByEmail(userDto.getEmail());

        if (userFromDb != null) {
            throw new RecordAlreadyExistsException("User with email '" + userFromDb.getEmail() + "' already exists");
        }

        UserEntity userEntity = new UserEntity();
        // copy the UserDto properties into UserEntity
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setUserId(utils.generateUserId());
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        UserEntity savedUser = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        // copy the UserEntity properties into UserDto
        BeanUtils.copyProperties(savedUser, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException(email);		// provided by Spring
        }

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
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
