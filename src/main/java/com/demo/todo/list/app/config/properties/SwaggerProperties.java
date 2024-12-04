package com.demo.todo.list.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private String applicationVersion;
    private String group;
    private String title;
    private String description;
    private String summary;
    private SecurityScheme security;


    @Data
    public static class SecurityScheme {
        private String schemes;
        private String scheme;
        private String bearerFormat;

    }

}
