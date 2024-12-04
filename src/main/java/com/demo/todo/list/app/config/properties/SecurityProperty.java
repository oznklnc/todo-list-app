package com.demo.todo.list.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Data
@ConfigurationProperties(prefix = "jwt.token")
public class SecurityProperty {
    private String secretKey;
    private Duration duration;



}
