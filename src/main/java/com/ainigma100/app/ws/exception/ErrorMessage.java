package com.ainigma100.app.ws.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private String message;
    private List<String> details;

}
