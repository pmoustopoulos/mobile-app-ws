package com.ainigma100.app.ws.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserLoginRequestModel implements Serializable {

    private String email;
    private String password;

}
