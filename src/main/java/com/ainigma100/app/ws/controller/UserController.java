package com.ainigma100.app.ws.controller;

import com.ainigma100.app.ws.dto.AddressDTO;
import com.ainigma100.app.ws.dto.UserDTO;
import com.ainigma100.app.ws.model.request.UserDetailsRequestModel;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import com.ainigma100.app.ws.model.response.AddressesResponseModel;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import com.ainigma100.app.ws.service.AddressService;
import com.ainigma100.app.ws.service.UserService;
import com.ainigma100.app.ws.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final Utils utils;


    @PostMapping("/pagination")
    public ResponseEntity<Page<UserDetailsResponseModel>> getUsersUsingPagination(
            @Valid @RequestBody UserSearchCriteria userSearchCriteria
            ) {

        return userService.getUsersUsingPagination(userSearchCriteria);
    }


    @GetMapping("/{id}")
    public UserDetailsResponseModel getUserByUserId(@PathVariable String id) {

        UserDTO userDto = userService.getUserByUserId(id);

        return utils.map(userDto, UserDetailsResponseModel.class);

    }


    @PostMapping
    public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

        UserDTO userDto = utils.map(userDetailsRequestModel, UserDTO.class);

        UserDTO createdUser = userService.createUser(userDto);

        return utils.map(createdUser, UserDetailsResponseModel.class);

    }


    @PutMapping("/{id}")
    public UserDetailsResponseModel updateUser(
            @PathVariable String id,
            @RequestBody UserDetailsRequestModel userDetailsRequestModel) {

        UserDTO userDto = utils.map(userDetailsRequestModel, UserDTO.class);

        UserDTO updatedUser = userService.updateUser(id, userDto);

        return utils.map(updatedUser, UserDetailsResponseModel.class);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return userService.deleteUserByUserId(id);
    }


    @GetMapping("/{id}/addresses")
    public List<AddressesResponseModel> getAddressesByUserId(@PathVariable String id) {

        List<AddressDTO> addressDTOList = addressService.getAddressesByUserId(id);

        return utils.mapList(addressDTOList, AddressesResponseModel.class);

    }


}
