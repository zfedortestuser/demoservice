package com.example.demoservice.order;

import com.example.demoservice.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Строка заказа, содержит ссылку на товар и его количество
 */
@Entity
public class OrderLine {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    @JsonIgnore
    private Order order;
    @ManyToOne(optional = false)
    @NotNull(message = "Please provide a product")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
