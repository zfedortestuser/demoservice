package com.example.demoservice.order;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class OrderScheduler {
    private final OrderService orderService;

    public OrderScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    void processScheduledOrders() {
        orderService.processScheduledOrders();
    }
}
