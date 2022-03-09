package com.ainigma100.app.ws.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864_000_000; // 10 days. Used when generating the token
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
//    public static final String SIGN_UP_URL = "/users";
    public static final String ISSUER = "Ainigma 100";
    public static final String AUDIENCE = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String JWT_SECRET = "ifh7ws456f9zq2t";
    public static final String[] PUBLIC_URLS = {
            "/users/login",
            "/users/password-reset-request",
            "/users/password-reset",
            "/users/email-verification",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/h2-console/**"
    };
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";

}
