package com.ainigma100.app.ws.utils.jwt;

import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.security.SecurityConstants;
import com.ainigma100.app.ws.security.UserPrincipal;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * This method is used to generate a JWT token
     * @param username
     * @return token as string
     */
    public String generateJwtToken(String username) {

        // we commented out this line because our UserEntity object does not have roles as a property
//        String[] claims = this.getClaimsFromUser(userEntity);

        return JWT.create()
                .withIssuer(SecurityConstants.ISSUER)
                .withAudience(SecurityConstants.AUDIENCE)
                .withIssuedAt(new Date())
                .withSubject(username) // each user has a unique email and it is used as username
//                .withArrayClaim(SecurityConstants.AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.JWT_SECRET.getBytes()));
    }

    /**
     * This method is used to extract the authorities from a token
     * @param token
     * @return list of GrantedAuthority
     */
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        String[] claims = this.getClaimsFromToken(token);

        // loop a collection using a stream and create a list of SimpleGrantedAuthority for every claim
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /**
     * This method is used to get an Authentication
     * @param userPrincipal
     * @param authorities
     * @param request
     * @return
     */
    public Authentication getAuthentication(
            UserPrincipal userPrincipal,
            List<GrantedAuthority> authorities,
            HttpServletRequest request) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;

    }

    /**
     * This method is used to check if a token is valid
     * @param username
     * @param token
     * @return true if valid and false otherwise
     */
    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = this.getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !this.isTokenExpired(verifier, token);
    }

    /**
     * This method is used to extract the username/subject from the token
     * @param token
     * @return subject/username
     */
    public String getSubject(String token) {
        JWTVerifier verifier = this.getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    /**
     * This method is used to check if a token has expired
     * @param verifier
     * @param token
     * @return true if token has expired and false otherwise
     */
    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }


    /**
     * This method checks if a token has expired
     *
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        JWTVerifier verifier = this.getJWTVerifier();
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }


//    private String[] getClaimsFromUser(UserEntity userEntity) {
//        List<String> authorities = new ArrayList<>();
//
//        for (GrantedAuthority grantedAuthority : userEntity.getAuthorities()) {
//            authorities.add(grantedAuthority.getAuthority());
//        }
//
//        // return the value as an array of string
//        return authorities.toArray(new String[0]);
//    }


    /**
     * This method is used to extract the claims from the token as a string array
     * @param token
     * @return
     */
    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = this.getJWTVerifier();

        return verifier.verify(token).getClaim(SecurityConstants.AUTHORITIES).asArray(String.class);
    }


    /**
     * This method is used to get a JWTVerifier using a specific Algorithm
     * @return
     */
    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;

        try {
            Algorithm algorithm = Algorithm.HMAC512(SecurityConstants.JWT_SECRET);
            verifier = JWT.require(algorithm).withIssuer(SecurityConstants.ISSUER).build();
        } catch (JWTVerificationException exception) {
            // we do not want to return many details and this is why we return our custom message
            throw new JWTVerificationException(SecurityConstants.TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

}
