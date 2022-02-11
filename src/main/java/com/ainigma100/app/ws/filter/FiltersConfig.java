package com.ainigma100.app.ws.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FiltersConfig {

    private final LoggingFilter loggingFilter;


    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterBean() {

        final FilterRegistrationBean<LoggingFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(loggingFilter);

        filterBean.addUrlPatterns("/*");
        // Lower values have higher priority
        filterBean.setOrder(Integer.MAX_VALUE-2);

        return filterBean;
    }

}