package com.demo.todo.list.app.controller;

import com.demo.todo.list.app.model.request.UserRegisterRequest;
import com.demo.todo.list.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("register")
    public String registerUser(@RequestBody UserRegisterRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("test")
    public String test() {
        return "Hello World!";
    }
}
