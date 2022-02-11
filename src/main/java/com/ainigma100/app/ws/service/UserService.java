package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.UserDTO;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDto);
    UserDTO getUser(String email);
    UserDTO getUserByUserId(String userId);
    UserDTO updateUser(String userId, UserDTO userDto);
    ResponseEntity<String> deleteUserByUserId(String id);

    ResponseEntity<Page<UserDetailsResponseModel>> getUsersUsingPagination(UserSearchCriteria userSearchCriteria);
}
