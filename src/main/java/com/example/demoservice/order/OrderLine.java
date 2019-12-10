package com.example.demoservice.order;

import com.example.demoservice.product.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Строка заказа, содержит ссылку на товар и его количество
 */
@Data
@Entity
public class OrderLine {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    @JsonManagedReference
    private Order order;
    @ManyToOne(optional = false)
    @NotEmpty(message = "Please provide a product")
    private Product product;
    @NotNull(message = "Please provide a quantity")
    @DecimalMin("0.0")
    private int quantity;

    public OrderLine() {
    }

    public OrderLine(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }
}
