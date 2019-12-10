package com.example.demoservice.product;

import com.example.demoservice.DemoServiceException;

public class ProductNotFoundException extends DemoServiceException {
    public ProductNotFoundException(Long id) {
        super("Product not found " + id);
    }
}