package com.example.demoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoserviceApplication.class, args);
    }
}
