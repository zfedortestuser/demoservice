package com.example.demoservice.order;

import com.example.demoservice.log.LogService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class OrderService {
    private final OrderRepository orderRepository;
    private final LogService logService;

    public OrderService(OrderRepository orderRepository, LogService logService) {
        this.orderRepository = orderRepository;
        this.logService = logService;
    }

    @Transactional
    public void processScheduledOrders() {
        List<Order> orders = orderRepository.findScheduledOrders(OrderStatus.SCHEDULED, new Date());
        if (!orders.isEmpty())
            logService.log("Найдено {} ожидающих заказов", orders.size());
        for (Order order : orders) {
            StringBuilder sb = new StringBuilder("Выполняю ");
            sb.append(order.isPeriodical() ? " периодический заказ " : "отложенный заказ ");
            sb.append(" пользователя ");
            sb.append(order.getUser().getName());
            sb.append(", заказываю ");
            for (OrderLine line : order.getLines()) {
                sb.append(line.getProduct().getName());
                sb.append(" в количестве ");
                sb.append(line.getQuantity());
                sb.append("шт. ");
            }
            logService.log(sb.toString());
            if (order.isPeriodical()) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, order.getDelay());
                order.setControlDate(calendar.getTime());
            } else {
                order.setStatus(OrderStatus.FINISHED);
            }
            orderRepository.save(order);
        }
    }
}
