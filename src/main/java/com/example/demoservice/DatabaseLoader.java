package com.example.demoservice;

import com.example.demoservice.log.LogServiceImpl;
import com.example.demoservice.product.Product;
import com.example.demoservice.product.ProductRepository;
import com.example.demoservice.user.User;
import com.example.demoservice.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseLoader {
    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   ProductRepository productRepository) {
        return args -> {
            logger.info("Preloading " + userRepository.save(new User("Бильбо")));
            logger.info("Preloading " + userRepository.save(new User("Фродо")));
            logger.info("Preloading " + productRepository.save(new Product("Молоко")));
            logger.info("Preloading " + productRepository.save(new Product("Хлеб")));
            logger.info("Preloading " + productRepository.save(new Product("Огурцы")));
            logger.info("Preloading " + productRepository.save(new Product("Помидоры")));
        };
    }
}