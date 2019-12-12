package com.example.demoservice.product;

import com.example.demoservice.order.OrderNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    Product findById(@PathVariable long id) {
        return productService.findById(id);
    }
}
