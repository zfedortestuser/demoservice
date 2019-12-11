package com.example.demoservice.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("from Order where status=?1 and controlDate < ?2")
    List<Order> findScheduledOrders(OrderStatus orderStatus, Date currentDate);
}
