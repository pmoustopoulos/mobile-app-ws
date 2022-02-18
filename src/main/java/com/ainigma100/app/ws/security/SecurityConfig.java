package com.ainigma100.app.ws.security;

import com.ainigma100.app.ws.filter.CustomAuthenticationFilter;
import com.ainigma100.app.ws.filter.CustomAuthorizationFilter;
import com.ainigma100.app.ws.service.UserService;
import com.ainigma100.app.ws.utils.jwt.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTTokenProvider jwtTokenProvider;


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // disable cross-side request forgery
        http.csrf().disable();
        // tell Spring Security that our REST API should be stateless we do not want to create an HTTP Session
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll();
        http.authorizeRequests().antMatchers(SecurityConstants.PUBLIC_URLS).permitAll();
        http.authorizeRequests().anyRequest().authenticated();                         // any other request should be authenticated
        http.addFilter(this.getAuthenticationFilter());     // add custom login URL
        http.addFilterBefore(new CustomAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * This method is used to modify the default login URL http://localhost:8088/login
     * into http://localhost:8088/users/login
     * @return
     * @throws Exception
     */
    public CustomAuthenticationFilter getAuthenticationFilter() throws Exception {
        final CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManagerBean(), jwtTokenProvider);
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }

}
