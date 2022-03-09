package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.UserDTO;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDto, HttpServletResponse response);
    UserDTO getUser(String email);
    UserDTO getUserById(String userId);
    UserDTO updateUser(String userId, UserDTO userDto);
    ResponseEntity<String> deleteUserById(String id);

    ResponseEntity<Page<UserDetailsResponseModel>> getUsersUsingPagination(UserSearchCriteria userSearchCriteria);

    boolean requestPasswordReset(String email, HttpServletResponse response);

    boolean resetPassword(String token, String newPassword);

    ByteArrayInputStream getExcelReport();

    boolean verifyEmailToken(String token);
}
