package com.ainigma100.app.ws.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserDTO implements Serializable {

    // id from the Database
    private long id;
    // public user id that will be sent back contained in the response
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    // set default value
    private Boolean emailVerificationStatus = false;

    private List<AddressDTO> addresses;

}
