package com.example.demoservice;

public class DemoServiceException extends RuntimeException {
    public DemoServiceException() {
    }

    public DemoServiceException(String message) {
        super(message);
    }

    public DemoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DemoServiceException(Throwable cause) {
        super(cause);
    }

    public DemoServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
