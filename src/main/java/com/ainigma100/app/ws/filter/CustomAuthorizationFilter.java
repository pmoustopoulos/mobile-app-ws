package com.ainigma100.app.ws.filter;

import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.repository.UserRepository;
import com.ainigma100.app.ws.security.SecurityConstants;
import com.ainigma100.app.ws.security.UserPrincipal;
import com.ainigma100.app.ws.utils.jwt.JWTTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // if user wants to log in do not do anything and just past to the next filter
        if (request.getServletPath().equals("/users/login")) {
            filterChain.doFilter(request, response);
        } else {

            // read the Header of the request
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {

                try {
                    // remove the 'Bearer ' prefix value from the authorizationHeader
                    String token = authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length());

                    // get the subject/username from the token
                    String username = jwtTokenProvider.getSubject(token);

                    if (jwtTokenProvider.isTokenValid(username, token)) {

                        UserEntity userEntity = userRepository.findByEmail(username);
                        UserPrincipal userPrincipal = new UserPrincipal(userEntity);

                        List<GrantedAuthority> authorities = new ArrayList<>(userPrincipal.getAuthorities());

                        Authentication authenticationToken = jwtTokenProvider.getAuthentication(userPrincipal, authorities, request);

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        SecurityContextHolder.clearContext();
                    }

                    filterChain.doFilter(request, response);

                } catch (Exception exception) {
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
//                    response.sendError(FORBIDDEN.value());

                    Map<String, String> error = new HashMap<>();
                    // we do not want to return many details and this is why we return our custom message
                    error.put("error_message", SecurityConstants.TOKEN_CANNOT_BE_VERIFIED);

                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }

            } else {
                // let the request continue to the next filter
                filterChain.doFilter(request, response);
            }

        } // end of ELSE

    } // end of doFilterInternal

}
