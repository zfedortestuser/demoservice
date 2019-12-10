package com.example.demoservice.order;

import com.example.demoservice.product.Product;
import com.example.demoservice.product.ProductNotFoundException;
import com.example.demoservice.product.ProductRepository;
import com.example.demoservice.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class OrderController {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderLineRepository orderLineRepository;

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
        return getOrder(id);
    }

    @PostMapping("/orders")
    Order create(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @PostMapping("/orders/{id}/addLine")
    OrderLine addLine(@PathVariable Long id, @Valid @RequestBody OrderLine orderLine) {
        Order order = getOrder(id);
        if (!order.getStatus().isCanAddLine()) {
            throw new InvalidOrderException("Нельзя изменять заказ в статусе " + order.getStatus());
        }
        Long productId = orderLine.getProduct().getId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        Optional<OrderLine> existingLine = order.getLines().stream().filter(line -> line.getProduct().equals(product)).findFirst();
        // если в заказе уже есть строчка с продуктом то просто увеличиваем количество в ней
        // иначе создаём новую строчку
        if (existingLine.isPresent()) {
            OrderLine line = existingLine.get();
            line.setQuantity(line.getQuantity() + orderLine.getQuantity());
            return line;
        } else {
            orderLine.setOrder(order);
            orderLine.setProduct(product);
            if (order.getStatus() == OrderStatus.NEW) {
                order.setStatus(OrderStatus.ACTUAL);
            }
            return orderLineRepository.save(orderLine);
        }
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PostMapping("/orders/{id}/finish")
    Order finishOrder(@PathVariable Long id) {
        Order order = getOrder(id);
        if (!order.getStatus().isCanFinish()) {
            throw new InvalidOrderException("Нельзя завершить заказ в статусе " + order.getStatus());
        }
        order.setStatus(OrderStatus.FINISHED);
        return order;
    }
}
