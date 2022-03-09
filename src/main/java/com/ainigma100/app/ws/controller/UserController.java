package com.ainigma100.app.ws.controller;

import com.ainigma100.app.ws.dto.AddressDTO;
import com.ainigma100.app.ws.dto.UserDTO;
import com.ainigma100.app.ws.model.request.PasswordResetModel;
import com.ainigma100.app.ws.model.request.PasswordResetRequestModel;
import com.ainigma100.app.ws.model.request.UserDetailsRequestModel;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import com.ainigma100.app.ws.model.response.AddressResponseModel;
import com.ainigma100.app.ws.model.response.FileResponse;
import com.ainigma100.app.ws.model.response.UserDetailsResponseModel;
import com.ainigma100.app.ws.service.AddressService;
import com.ainigma100.app.ws.service.UserService;
import com.ainigma100.app.ws.swagger.SwaggerConstants;
import com.ainigma100.app.ws.utils.Utils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api(tags = {SwaggerConstants.API_TAG})
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

        UserDTO userDto = userService.getUserById(id);

        return utils.map(userDto, UserDetailsResponseModel.class);

    }


    @PostMapping
    public UserDetailsResponseModel createUser(
            @RequestBody UserDetailsRequestModel userDetailsRequestModel,
            HttpServletResponse response) {

        UserDTO userDto = utils.map(userDetailsRequestModel, UserDTO.class);

        UserDTO createdUser = userService.createUser(userDto, response);

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
        return userService.deleteUserById(id);
    }


    @GetMapping("/{userId}/addresses")
    public List<AddressResponseModel> getAddressesByUserId(@PathVariable String userId) {

        List<AddressDTO> addressDTOList = addressService.getAddressesByUserId(userId);

        return utils.mapList(addressDTOList, AddressResponseModel.class);

    }

    @GetMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<AddressResponseModel> getUserAddressByAddressId(
            @PathVariable String userId,
            @PathVariable String addressId
    ) {

        AddressDTO addressDTO = addressService.getUserAddressById(userId, addressId);

        AddressResponseModel returnValue = utils.map(addressDTO, AddressResponseModel.class);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<String> requestPasswordReset(
            @RequestBody PasswordResetRequestModel passwordResetRequestModel,
            HttpServletResponse response) {

        boolean isPasswordReset = userService.requestPasswordReset(passwordResetRequestModel.getEmail(), response);

        if (isPasswordReset) {
            return new ResponseEntity<>("Password reset request has been sent", HttpStatus.OK);
        }

        return new ResponseEntity<>("Password reset request failed", HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword(
            @RequestBody PasswordResetModel passwordResetModel) {

        boolean isPasswordReset = userService.resetPassword(
                passwordResetModel.getToken(),
                passwordResetModel.getNewPassword());

        if (isPasswordReset) {
            return new ResponseEntity<>("Password has been reset", HttpStatus.OK);
        }

        return new ResponseEntity<>("Password reset failed", HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/report")
    public ResponseEntity<FileResponse> getExcelReportAsEncodedValue() throws IOException {

        ByteArrayInputStream byteArrayInputStream = userService.getExcelReport();

        int n = byteArrayInputStream.available();
        byte[] bytes = new byte[n];
        byteArrayInputStream.read(bytes, 0, n);

        String base64String = Base64.encodeBase64String(bytes);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fileName = localDateTime.format(format).concat("_User_Report.xlsx");

        FileResponse fileResponse = new FileResponse();
        fileResponse.setFile(base64String);
        fileResponse.setFileName(fileName);

        return new ResponseEntity<>(fileResponse, HttpStatus.OK);
    }

    @GetMapping("/download-report")
    public ResponseEntity<InputStreamResource> downloadExcelReport() throws IOException {

        ByteArrayInputStream byteArrayInputStream = userService.getExcelReport();


        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fileName = localDateTime.format(format).concat("_User_Report.xlsx");

        InputStream targetStream = byteArrayInputStream;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=".concat(fileName));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(new InputStreamResource(targetStream));
    }

    @GetMapping("/email-verification")
    public ResponseEntity<String> verifyEmailToken(@RequestParam(value = "token") String token) {

        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified) {
            return new ResponseEntity<>("Your email has been verified", HttpStatus.OK);
        }

        return new ResponseEntity<>("Your email has not been verified", HttpStatus.BAD_REQUEST);
    }

}

