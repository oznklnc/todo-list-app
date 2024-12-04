package com.demo.todo.list.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "couchbase")
public class CouchbaseProperties {

    private String connectionString;
    private String username;
    private String password;
    private String bucketName;
    private String scopeName;
}
