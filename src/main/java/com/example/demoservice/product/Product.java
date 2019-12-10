package com.example.demoservice.product;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Требуется название товара")
    private String name;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }
}
