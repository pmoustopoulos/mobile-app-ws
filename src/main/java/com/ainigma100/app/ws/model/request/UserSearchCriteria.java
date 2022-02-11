package com.ainigma100.app.ws.model.request;

import com.ainigma100.app.ws.utils.SortItem;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.List;

@Getter
public class UserSearchCriteria implements Serializable {

    @NotNull(message = "page cannot be null")
    @PositiveOrZero(message = "page must be a zero or a positive number")
    private Integer page;

    @NotNull(message = "size cannot be null")
    @Positive(message = "size must be a positive number")
    private Integer size;

    private String firstName;
    private String lastName;
    private String email;

    @NotNull(message = "sortList can be an empty list but not null")
    private List<SortItem> sortList;

}
