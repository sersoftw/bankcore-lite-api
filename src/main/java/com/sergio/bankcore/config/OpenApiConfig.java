package com.sergio.bankcore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI bankcoreOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("BankCore Lite API")
                        .version("1.0.0")
                        .description("API REST bancaria con Java 17, Spring Boot, JPA, H2, Swagger, tests y CI/CD básico.")
                        .contact(new Contact().name("Sergio Bernal Galvez")));
    }
}
