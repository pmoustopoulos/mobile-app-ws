package com.ainigma100.app.ws.controller;

import com.ainigma100.app.ws.model.request.UserLoginRequestModel;
import com.ainigma100.app.ws.swagger.SwaggerConstants;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthenticationController {

    @ApiOperation("User Login")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Response Headers",
                    responseHeaders = {
                        @ResponseHeader(name = "authorization",
                                description = "Bearer <JWT value here>",
                                response = String.class),
                        @ResponseHeader(name = "id",
                                description = "User id value here",
                                response = String.class)
                    }
            )
    })
    @PostMapping("/users/login")
    public void theFakeLogin(@RequestBody UserLoginRequestModel loginRequestModel) {
        throw new IllegalStateException("This method should not be called. This method is implemented by Spring Security");
    }

}
