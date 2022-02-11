package com.ainigma100.app.ws.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressesResponseModel implements Serializable {

    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;

}
