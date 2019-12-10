package com.example.demoservice.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidOrderAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String invalidOrderExceptionHandler(InvalidOrderException ex) {
        return ex.getMessage();
    }
}
