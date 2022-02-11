package com.ainigma100.app.ws.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressRequestModel implements Serializable {

    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;

}
