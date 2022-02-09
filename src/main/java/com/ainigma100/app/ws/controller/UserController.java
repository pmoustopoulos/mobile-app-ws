package com.ainigma100.app.ws.controller;

import com.ainigma100.app.ws.dto.UserDto;
import com.ainigma100.app.ws.model.request.UserDetailsRequestModel;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import com.ainigma100.app.ws.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @GetMapping
    public String getUser() {
        return "Get user was called";
    }

    @PostMapping
    public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

        UserDetailsResponseModel returnValue = new UserDetailsResponseModel();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto createUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser() {
        return "Update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "Delete user was called";
    }



}

