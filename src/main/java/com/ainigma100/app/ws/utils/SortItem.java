package com.ainigma100.app.ws.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.Sort.Direction;

import java.io.Serializable;

@Getter
public class SortItem implements Serializable {

    @ApiModelProperty(example = "firstName")
    private String field;
    private Direction direction;

}
