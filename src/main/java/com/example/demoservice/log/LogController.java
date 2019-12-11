package com.example.demoservice.log;

import com.example.demoservice.order.*;
import com.example.demoservice.product.Product;
import com.example.demoservice.product.ProductNotFoundException;
import com.example.demoservice.product.ProductRepository;
import com.example.demoservice.user.User;
import com.example.demoservice.user.UserNotFoundException;
import com.example.demoservice.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
public class LogController {
    private final LogRepository logRepository;

    public LogController(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping("/logs")
    List<Log> all() {
        return logRepository.findAllSortedById();
    }
}
