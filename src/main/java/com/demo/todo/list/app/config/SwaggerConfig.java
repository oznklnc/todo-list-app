package com.demo.todo.list.app.config;

import com.demo.todo.list.app.config.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group(swaggerProperties.getGroup())
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(swaggerProperties.getTitle())
                                .version(swaggerProperties.getApplicationVersion())
                                .description(swaggerProperties.getDescription())
                                .summary(swaggerProperties.getSummary())
                )
                .components(
                        new Components()
                                .addSecuritySchemes(swaggerProperties.getSecurity().getSchemes(), createAPIKeyScheme())
                );
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(swaggerProperties.getSecurity().getScheme())
                .bearerFormat(swaggerProperties.getSecurity().getBearerFormat());
    }
}
