package com.ainigma100.app.ws.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserDetailsRequestModel implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
