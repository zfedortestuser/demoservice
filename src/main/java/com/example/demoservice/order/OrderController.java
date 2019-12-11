package com.example.demoservice.order;

import com.example.demoservice.product.Product;
import com.example.demoservice.product.ProductNotFoundException;
import com.example.demoservice.product.ProductRepository;
import com.example.demoservice.user.User;
import com.example.demoservice.user.UserNotFoundException;
import com.example.demoservice.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Stream;

@RestController
public class OrderController {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderLineRepository orderLineRepository;

    public OrderController(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository, OrderLineRepository orderLineRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderLineRepository = orderLineRepository;
    }

    @GetMapping("/orders")
    List<Order> all() {
        return orderRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    Order one(@PathVariable Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PostMapping("/orders/{id}/addProducts")
    OrderLine addProducts(@PathVariable Long id,
                          @RequestParam() Long productId,
                          @RequestParam() Integer quantity) {
        Order order = getOrder(id);
        if (!order.getStatus().isCanAddLine()) {
            throw new InvalidOrderException("Cannot change order with status " + order.getStatus());
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        Optional<OrderLine> existingLine = order.getLines().stream().filter(line -> line.getProduct().equals(product)).findFirst();
        // если в заказе уже есть строчка с продуктом то просто увеличиваем количество в ней
        // иначе создаём новую строчку
        if (existingLine.isPresent()) {
            OrderLine line = existingLine.get();
            line.setQuantity(line.getQuantity() + quantity);
            orderLineRepository.save(line);
            return line;
        } else {
            if (order.getStatus() == OrderStatus.NEW) {
                order.setStatus(OrderStatus.ACTUAL);
                orderRepository.save(order);
            }
            OrderLine orderLine = new OrderLine(order, product, quantity);
            return orderLineRepository.save(orderLine);
        }
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PostMapping("/orders")
    Order create(@RequestParam(name = "userId") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Order order = new Order(user);
        order.setStatus(OrderStatus.NEW);
        return orderRepository.save(order);
    }

    @PostMapping("/orders/{id}/finish")
    Order finishOrder(@PathVariable Long id) {
        Order order = getOrder(id);
        if (!order.getStatus().isCanFinish()) {
            throw new InvalidOrderException("Cannot finish order with status " + order.getStatus());
        }
        order.setStatus(OrderStatus.FINISHED);
        orderRepository.save(order);
        return order;
    }

    @PostMapping("/orders/{id}/schedule")
    Order scheduleOnce(@PathVariable Long id,
                       @RequestParam() int delay,
                       @RequestParam() boolean periodical) {
        Order order = getOrder(id);
        if (!order.getStatus().isCanFinish()) {
            throw new InvalidOrderException("Cannot schedule order with status " + order.getStatus());
        }
        order.setStatus(periodical ? OrderStatus.SCHEDULED_PERIODICALLY : OrderStatus.SCHEDULED_ONCE);
        order.setDelay(delay);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, delay);
        order.setControlDate(calendar.getTime());
        orderRepository.save(order);
        return order;
    }
}
