package com.example.demoservice.order;

import com.example.demoservice.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ORDER_TABLE")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JsonManagedReference
    private User user;
    private OrderStatus status = OrderStatus.NEW;
    @OneToMany(mappedBy = "order")
    @OrderBy("id")
    @JsonBackReference
    private List<OrderLine> lines;

    public Order() {
    }

    public Order(User user) {
        this.user = user;
    }
}
