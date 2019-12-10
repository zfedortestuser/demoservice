package com.example.demoservice.user;

import com.example.demoservice.DemoServiceException;

public class UserNotFoundException extends DemoServiceException {
    public UserNotFoundException(Long id) {
        super("User not found " + id);
    }
}