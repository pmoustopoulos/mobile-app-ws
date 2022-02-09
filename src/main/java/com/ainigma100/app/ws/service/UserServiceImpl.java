package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.UserDto;
import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.exception.RecordAlreadyExistsException;
import com.ainigma100.app.ws.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public UserDto createUser(UserDto userDto) {

        UserEntity userFromDb = userRepository.findUserByEmail(userDto.getEmail());

        if (userFromDb != null) {
            throw new RecordAlreadyExistsException("User with email '" + userFromDb.getEmail() + "' already exists");
        }

        UserEntity userEntity = new UserEntity();
        // copy the UserDto properties into UserEntity
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setUserId("testUserId");
        userEntity.setEncryptedPassword("test");

        UserEntity savedUser = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        // copy the UserEntity properties into UserDto
        BeanUtils.copyProperties(savedUser, returnValue);

        return returnValue;
    }

}
