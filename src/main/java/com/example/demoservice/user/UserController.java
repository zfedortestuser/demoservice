package com.example.demoservice.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    User findById(@PathVariable long id) {
        return userService.findById(id);
    }
}
