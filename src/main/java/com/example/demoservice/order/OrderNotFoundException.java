package com.example.demoservice.order;

import com.example.demoservice.DemoServiceException;

public class OrderNotFoundException extends DemoServiceException {
    public OrderNotFoundException(Long id) {
        super("Order not found " + id);
    }
}