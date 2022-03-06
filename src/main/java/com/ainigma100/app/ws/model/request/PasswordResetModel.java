package com.ainigma100.app.ws.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PasswordResetModel implements Serializable {

    private String token;
    private String newPassword;

}
