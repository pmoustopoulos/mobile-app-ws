package com.ainigma100.app.ws.utils;

import lombok.*;
import org.springframework.data.domain.Sort.Direction;

import java.io.Serializable;

@Getter
public class SortItem implements Serializable {

    private String field;
    private Direction direction;

}
