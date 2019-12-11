package com.example.demoservice.order;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OrderScheduler {
    private static final Logger logger = LoggerFactory.getLogger(OrderScheduler.class);
    private final OrderRepository orderRepository;

    public OrderScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = 5000)
    void processScheduledOrders() {
        logger.debug("Processing scheduled orders...");
        //orderRepository.
    }
}
