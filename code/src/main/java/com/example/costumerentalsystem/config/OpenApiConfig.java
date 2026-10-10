package com.example.costumerentalsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI costumeRentalOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Costume Rental System API")
                .version("v1")
                .description("REST API for costume catalog and rental lifecycle."));
    }
}
