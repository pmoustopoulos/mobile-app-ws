package com.ainigma100.app.ws.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserDetailsResponseModel implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String email;

    private List<AddressResponseModel> addresses;

}
