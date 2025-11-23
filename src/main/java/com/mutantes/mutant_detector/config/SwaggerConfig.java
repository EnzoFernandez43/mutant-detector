package com.mutantes.mutant_detector.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mutantApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API")
                        .description("API REST para detección de mutantes analizando secuencias de ADN.")
                        .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("mutants")
                .pathsToMatch("/mutant/**", "/stats/**")
                .build();
    }
}