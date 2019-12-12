package com.example.demoservice.order;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    List<Order> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/orders/{id}")
    Order findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping("/orders/{id}/addProducts")
    OrderLine addProducts(@PathVariable long id,
                          @RequestParam() long productId,
                          @RequestParam() int quantity) {
        return orderService.addProducts(id, productId, quantity);
    }

    @PostMapping("/orders")
    Order createOrder(@RequestParam long userId) {
        return orderService.createOrder(userId);
    }

    @PostMapping("/orders/{id}/finish")
    Order finishOrder(@PathVariable long id) {
        return orderService.finishOrder(id);
    }

    @PostMapping("/orders/{id}/schedule")
    Order schedule(@PathVariable long id,
                   @RequestParam int delay,
                   @RequestParam boolean periodical) {
        return orderService.scheduleOrder(id, delay, periodical);
    }
}
