package com.ainigma100.app.ws.swagger;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.Collections;
import java.util.List;

import static com.ainigma100.app.ws.swagger.SwaggerConstants.*;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiDocket() {
        return new Docket(SWAGGER_2)
                .apiInfo(this.apiInfo())
                .forCodeGeneration(true)
                .securityContexts(singletonList(this.securityContext()))
                .securitySchemes(singletonList(this.apiKey()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.regex(SECURE_PATH))
                .build()
                .tags(new Tag(API_TAG, "All APIs relating to User"));
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(APP_TITLE, APP_DESCRIPTION, API_VERSION, TERM_OF_SERVICE,
                null, LICENSE, LICENSE_URL, Collections.emptyList());
    }

    // Tell Swagger that we are going to use an API key which will be
    // in the header of the request
    private ApiKey apiKey() {
        return new ApiKey(SECURITY_REFERENCE, AUTHORIZATION, SecurityScheme.In.HEADER.name());
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(this.securityReference()).build();
    }


    // define the Authorization Scope and the Description
    private List<SecurityReference> securityReference() {
        AuthorizationScope[] authorizationScope = { new AuthorizationScope(AUTHORIZATION_SCOPE, AUTHORIZATION_DESCRIPTION) };
        return singletonList(new SecurityReference(SECURITY_REFERENCE, authorizationScope));
    }


    /**
     * This method enables us to modify the ui in swagger.
     * There are many things we can modify but now we just hide the models from the ui.
     * https://springfox.github.io/springfox/docs/current/#springfox-spring-mvc-and-spring-boot
     *
     * @return
     */
    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .defaultModelsExpandDepth(-1)
                .build();
    }

}
