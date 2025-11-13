package com.example.bankcards.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank REST API")
                        .description("API для управления картами и переводами")
                        .version("1.0.0"));
    }
}