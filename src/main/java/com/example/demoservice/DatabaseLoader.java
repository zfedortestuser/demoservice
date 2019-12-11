package com.example.demoservice;

import com.example.demoservice.order.*;
import com.example.demoservice.product.Product;
import com.example.demoservice.user.User;
import com.example.demoservice.product.ProductRepository;
import com.example.demoservice.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DatabaseLoader {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   ProductRepository productRepository) {
        return args -> {
            log.info("Preloading " + userRepository.save(new User("Bilbo Baggins")));
            log.info("Preloading " + userRepository.save(new User("Frodo Baggins")));
            log.info("Preloading " + productRepository.save(new Product("milk")));
            log.info("Preloading " + productRepository.save(new Product("bread")));
            log.info("Preloading " + productRepository.save(new Product("огурцы")));
            log.info("Preloading " + productRepository.save(new Product("помидоры")));
        };
    }
}