package com.demo.todo.list.app.generator;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TreeIdGenerator {

    public String generateTreeId() {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
