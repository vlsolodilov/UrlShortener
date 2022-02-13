package com.example.urlshortener.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

@SecurityScheme(
    type = SecuritySchemeType.APIKEY,
    name = "Authorization",
    in = SecuritySchemeIn.HEADER
)

@OpenAPIDefinition(
    info = @Info(
        title = "REST API documentation",
        version = "1.0",
        description = "Application YourBestLunch (graduation project for the course <a href='https://javaops.ru/view/topjava'> TopJava</a>)",
        contact = @Contact(url = "https://github.com/vlsolodilov", name = "Vladislav Solodilov", email = "vlsolodilov@gmail.com")
    ),
    security = @SecurityRequirement(name = "Authorization")
)
public class OpenApiConfig {

  @Bean
  public GroupedOpenApi api() {
    return GroupedOpenApi.builder()
        .group("REST API")
        .pathsToMatch("/api/**")
        .build();
  }
}
