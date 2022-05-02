package com.ainigma100.app.ws.model.request;

import com.ainigma100.app.ws.utils.SortItem;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class UserSearchCriteria implements Serializable {

    @ApiModelProperty(example = "0", required = true, position = 4)
    @NotNull(message = "page cannot be null")
    @PositiveOrZero(message = "page must be a zero or a positive number")
    private Integer page;

    @ApiModelProperty(example = "10", required = true, position = 5)
    @NotNull(message = "size cannot be null")
    @Positive(message = "size must be a positive number")
    private Integer size;

    @ApiModelProperty(notes = "Firstname of the user",
            example = "John", required = false, position = 1)
    private String firstName;

    @ApiModelProperty(notes = "Lastname of the user",
            example = "Wick", required = false, position = 2)
    private String lastName;

    @ApiModelProperty(notes = "Email address of the user",
            example = "jwick@gmail.com", required = false, position = 3)
    private String email;

    @NotNull(message = "sortList can be an empty list but not null")
    private List<SortItem> sortList;

}
