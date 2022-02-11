package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.UserDto;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUser(String email);
    UserDto getUserByUserId(String userId);
    UserDto updateUser(String userId, UserDto userDto);
    ResponseEntity<String> deleteUserByUserId(String id);

    ResponseEntity<Page<UserDetailsResponseModel>> getUsersUsingPagination(UserSearchCriteria userSearchCriteria);
}
