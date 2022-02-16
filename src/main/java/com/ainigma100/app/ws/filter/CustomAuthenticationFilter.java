package com.ainigma100.app.ws.filter;

import com.ainigma100.app.ws.model.request.UserLoginRequestModel;
import com.ainigma100.app.ws.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {

            // The body from the JSON request will be used to create UserLoginRequestModel
            UserLoginRequestModel credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequestModel.class);



            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());

            // authenticationManager will authenticate the user based on email and password
            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // If the user is authenticated, this method will run
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        String userName = ((User) authentication.getPrincipal()).getUsername();

        // algorithm used to sign the JSON web token
        Algorithm algorithm = Algorithm.HMAC256(SecurityConstants.JWT_SECRET.getBytes());

        // here we are building the JSON Access Web Token
        String accessToken = JWT.create()
                .withSubject(userName)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);	// signing the Web Token


        // here we are building the JSON Refresh Web Token
        String refreshToken = JWT.create()
                .withSubject(userName)
                .withExpiresAt(new Date(System.currentTimeMillis() + (SecurityConstants.EXPIRATION_TIME * 3) ))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);	// signing the Web Token



        // add the information to the header
//        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);
//        response.addHeader("RefreshToken", SecurityConstants.TOKEN_PREFIX + refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

    }





}
