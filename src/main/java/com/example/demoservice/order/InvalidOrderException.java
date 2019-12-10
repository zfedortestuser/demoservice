package com.example.demoservice.order;

import com.example.demoservice.DemoServiceException;

public class InvalidOrderException extends DemoServiceException {
    public InvalidOrderException(String message) {
        super(message);
    }
}
