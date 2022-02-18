package com.ainigma100.app.ws.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressRequestModel implements Serializable {

    @ApiModelProperty(example = "Athens", required = true, position = 1)
    private String city;

    @ApiModelProperty(example = "Greece", required = true, position = 2)
    private String country;

    @ApiModelProperty(example = "Nikis 2", required = true, position = 3)
    private String streetName;

    @ApiModelProperty(example = "52430", required = true, position = 4)
    private String postalCode;

    @ApiModelProperty(example = "billing", required = true, position = 5)
    private String type;

}
