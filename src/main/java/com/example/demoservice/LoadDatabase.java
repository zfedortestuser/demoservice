package com.example.demoservice;


import com.example.demoservice.product.Product;
import com.example.demoservice.order.Order;
import com.example.demoservice.user.User;
import com.example.demoservice.product.ProductRepository;
import com.example.demoservice.order.OrderRepository;
import com.example.demoservice.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        return args -> {
            log.info("Preloading " + userRepository.save(new User("Bilbo Baggins")));
            User frodoBaggins = new User("Frodo Baggins");
            log.info("Preloading " + userRepository.save(frodoBaggins));
            orderRepository.save(new Order(frodoBaggins));
            orderRepository.save(new Order(frodoBaggins));
            log.info("Preloading " + productRepository.save(new Product("молоко")));
            log.info("Preloading " + productRepository.save(new Product("хлеб")));
        };
    }
}