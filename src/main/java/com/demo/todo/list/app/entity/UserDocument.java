package com.demo.todo.list.app.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String email;
    private String password;
}
