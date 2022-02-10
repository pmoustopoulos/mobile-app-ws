package com.ainigma100.app.ws.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utils {

    public String generateUserId() {
        return UUID.randomUUID().toString();
    }


}
