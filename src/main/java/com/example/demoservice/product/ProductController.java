package com.example.demoservice.product;

import com.example.demoservice.order.OrderNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    List<Product> all() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    Product one(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PostMapping("/products")
    Product create(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }
}
