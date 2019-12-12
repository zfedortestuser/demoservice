package com.example.demoservice.order;

import com.example.demoservice.log.LogService;
import com.example.demoservice.product.Product;
import com.example.demoservice.product.ProductService;
import com.example.demoservice.user.User;
import com.example.demoservice.user.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final LogService logService;
    private final ProductService productService;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, LogService logService, ProductService productService, UserService userService) {
        this.orderRepository = orderRepository;
        this.logService = logService;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public Order createOrder(long userId) {
        User user = userService.findById(userId);
        Order order = new Order(user);
        order.setStatus(OrderStatus.NEW);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public OrderLine addProducts(long orderId, long productId, int quantity) {
        Order order = findById(orderId);
        if (!order.getStatus().isCanAddLine()) {
            throw new InvalidOrderException("Нельзя добавить продукты в заказ в статусе " + order.getStatus());
        }
        Product product = productService.findById(productId);
        Optional<OrderLine> existingLine = order.getLines().stream().filter(line -> line.getProduct().equals(product)).findFirst();
        // если в заказе уже есть строчка с продуктом то просто увеличиваем количество в ней
        // иначе создаём новую строчку
        if (existingLine.isPresent()) {
            OrderLine line = existingLine.get();
            line.setQuantity(line.getQuantity() + quantity);
            return line;
        } else {
            if (order.getStatus() == OrderStatus.NEW) {
                order.setStatus(OrderStatus.ACTUAL);
            }
            OrderLine line = new OrderLine(order, product, quantity);
            order.getLines().add(line);
            return line;
        }
    }

    @Override
    public Order finishOrder(long id) {
        Order order = findById(id);
        if (!order.getStatus().isCanFinish()) {
            throw new InvalidOrderException("Нельзя завершить заказ в статусе " + order.getStatus());
        }
        logService.log("Завершаю заказ %d пользователя %s", order.getId(), order.getUser().getName());
        processOrder(order);
        order.setStatus(OrderStatus.FINISHED);
        return order;
    }

    private void processOrder(Order order) {
        logService.log("Заказываю %s",
                order.getLines().stream()
                        .map(line -> String.format("%s в количестве %d шт.", line.getProduct().getName(), line.getQuantity()))
                        .collect(Collectors.joining(", ")));
    }

    @Override
    public Order scheduleOrder(long id, int delay, boolean periodical) {
        Order order = findById(id);
        if (!order.getStatus().isCanFinish()) {
            throw new InvalidOrderException("Cannot schedule order with status " + order.getStatus());
        }
        order.setStatus(OrderStatus.SCHEDULED);
        order.setDelay(delay);
        order.setPeriodical(periodical);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, delay);
        order.setControlDate(calendar.getTime());
        return order;
    }

    @Override
    public void processScheduledOrders() {
        List<Order> orders = orderRepository.findScheduledOrders(OrderStatus.SCHEDULED, new Date());
        for (Order order : orders) {
            logService.log("Найден %s заказ %d пользователя %s для выполнения",
                    order.isPeriodical() ? " периодический заказ" : "отложенный заказ",
                    order.getId(),
                    order.getUser().getName());
            if (order.isPeriodical()) {
                processOrder(order);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, order.getDelay());
                order.setControlDate(calendar.getTime());
            } else {
                finishOrder(order.getId());
            }
        }
    }
}
