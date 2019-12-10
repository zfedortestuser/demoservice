package com.example.demoservice.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    List<User> all() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/users")
    User create(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }
}
