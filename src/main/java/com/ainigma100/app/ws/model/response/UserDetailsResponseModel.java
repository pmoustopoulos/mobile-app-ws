package com.ainigma100.app.ws.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDetailsResponseModel implements Serializable {

    // this is not the actual id. It is a public user key
    private String userId;
    private String firstName;
    private String lastName;
    private String email;

}
