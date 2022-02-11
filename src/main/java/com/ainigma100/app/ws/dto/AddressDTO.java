package com.ainigma100.app.ws.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressDTO implements Serializable {

    private long id;
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    // we are creating bidirectional relationship and we need access from both sides
    private UserDTO userDetails;

}
