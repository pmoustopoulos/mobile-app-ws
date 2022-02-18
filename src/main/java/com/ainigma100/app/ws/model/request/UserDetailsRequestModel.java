package com.ainigma100.app.ws.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class UserDetailsRequestModel implements Serializable {

    @ApiModelProperty(notes = "Firstname of the user",
            example = "John", required = true, position = 1)
    private String firstName;

    @ApiModelProperty(notes = "Lastname of the user",
            example = "Wick", required = true, position = 2)
    private String lastName;

    @ApiModelProperty(notes = "Email address of the user",
            example = "jwick@gmail.com", required = false, position = 3)
    private String email;

    @ApiModelProperty(notes = "Password of the user",
            example = "123", required = true, position = 4)
    private String password;

    private List<AddressRequestModel> addresses;

}
