package com.ainigma100.app.ws.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String h2Console = "h2-console";

        // we do not want to print logs when we access h2 database
        if (!httpServletRequest.getServletPath().contains(h2Console)) {
            log.info("Request " + httpServletRequest.getRequestURL().toString() + ", method: " + httpServletRequest.getMethod());
            log.info("from uri: " + httpServletRequest.getRemoteAddr() + ", host: " + httpServletRequest.getRemoteHost());

        }

        //pre methods call stamps
        chain.doFilter(request, response);

        // we do not want to print logs when we access h2 database
        if (!httpServletRequest.getServletPath().contains(h2Console)) {
            log.info("Response status: " + httpServletResponse.getStatus());
        }

    }

}
