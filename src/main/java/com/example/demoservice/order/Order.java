package com.example.demoservice.order;

import com.example.demoservice.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDER_TABLE")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @NotNull
    private User user;
    private OrderStatus status = OrderStatus.NEW;
    @OneToMany(mappedBy = "order")
    @OrderBy("id")
    private List<OrderLine> lines;
    /**
     * Время следующего выполнения заказа
     */
    private Date controlDate;
    /**
     * Задержка перед выполнением отложенного заказа в секундах,
     * Период для периодического заказа
     */
    private int delay;

    public Order() {
    }

    public Order(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public void setLines(List<OrderLine> lines) {
        this.lines = lines;
    }

    public Date getControlDate() {
        return controlDate;
    }

    public void setControlDate(Date controlDate) {
        this.controlDate = controlDate;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
