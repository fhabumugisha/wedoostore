package com.wedogift.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * Configuration of swagger
 *
 */
@Configuration

public class SwaggerConfig {
    /**
     * Config of servers
     * @return
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url("http://localhost:8080/")
                        .description("Local  Server"))
                ;
    }
}