package com.example.demoservice.order;

import com.example.demoservice.log.LogRepository;
import com.example.demoservice.log.LogService;
import com.example.demoservice.log.LogServiceImpl;
import com.example.demoservice.product.Product;
import com.example.demoservice.product.ProductRepository;
import com.example.demoservice.product.ProductService;
import com.example.demoservice.product.ProductServiceImpl;
import com.example.demoservice.user.User;
import com.example.demoservice.user.UserRepository;
import com.example.demoservice.user.UserService;
import com.example.demoservice.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final LogRepository logRepository = Mockito.mock(LogRepository.class);

    private LogService logService;
    private ProductService productService;
    private UserService userService;
    private OrderService orderService;

    @BeforeEach
    void init() {
        logService = new LogServiceImpl(logRepository);
        userService = new UserServiceImpl(userRepository);
        productService = new ProductServiceImpl(productRepository);
        orderService = new OrderServiceImpl(orderRepository, logService, productService, userService);

        AtomicLong idGenerator = new AtomicLong(0);

        Map<Long, User> users = new HashMap<>();
        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> {
                    User user = (User) i.getArguments()[0];
                    user.setId(idGenerator.incrementAndGet());
                    users.put(user.getId(), user);
                    return user;
                });
        when(userRepository.findById(Mockito.anyLong()))
                .thenAnswer(i -> Optional.ofNullable(users.get(i.getArgument(0))));
        when(userRepository.findAll()).thenAnswer(i -> new ArrayList<>(users.values()));

        userRepository.save(new User("bilbo"));
        userRepository.save(new User("frodo"));

        Map<Long, Product> products = new HashMap<>();
        when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(i -> {
                    Product product = (Product) i.getArguments()[0];
                    product.setId(idGenerator.incrementAndGet());
                    products.put(product.getId(), product);
                    return product;
                });
        when(productRepository.findById(Mockito.anyLong()))
                .thenAnswer(i -> Optional.ofNullable(products.get(i.getArgument(0))));
        when(productRepository.findAll()).thenAnswer(i -> new ArrayList<>(products.values()));
        productRepository.save(new Product("apple"));
        productRepository.save(new Product("potato"));

        Map<Long, Order> orders = new HashMap<>();
        when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(i -> {
                    Order order = (Order) i.getArguments()[0];
                    order.setId(idGenerator.incrementAndGet());
                    order.setLines(new ArrayList<>());
                    orders.put(order.getId(), order);
                    return order;
                });
        when(orderRepository.findById(Mockito.anyLong()))
                .thenAnswer(i -> Optional.ofNullable(orders.get(i.getArgument(0))));
    }

    @Test
    void testCreateOrder() {
        User user = userService.findAll().get(0);
        Order order = orderService.createOrder(user.getId());
        assertEquals(order.getStatus(), OrderStatus.NEW);
        assertEquals(order.getUser(), user);
    }

    @Test
    void testAddProducts() {
        User user = userService.findAll().get(0);
        Order order = orderService.createOrder(user.getId());
        Product product = productService.findAll().get(0);
        int quantity = 5;
        OrderLine line = orderService.addProducts(order.getId(), product.getId(), quantity);
        assertEquals(line.getQuantity(), quantity);
        assertEquals(line.getOrder(), order);
        assertEquals(line.getProduct(), product);

        // добавление ранее добавленного товара увеличивает его количество
        line = orderService.addProducts(order.getId(), product.getId(), quantity);
        assertEquals(line.getQuantity(), quantity * 2);
    }

    @Test
    void testFinishOrder() {
        User user = userService.findAll().get(0);
        Order order = orderService.createOrder(user.getId());
        assertThrows(InvalidOrderException.class, () -> orderService.finishOrder(order.getId()));

        Product product = productService.findAll().get(0);
        orderService.addProducts(order.getId(), product.getId(), 5);

        Order order2 = orderService.finishOrder(order.getId());
        assertEquals(order, order2);
        assertEquals(order.getStatus(), OrderStatus.FINISHED);
    }

    @Test
    void testSchedule() {
        User user = userService.findAll().get(0);
        Order order = orderService.createOrder(user.getId());
        int delay = 5;
        boolean periodical = false;
        assertThrows(InvalidOrderException.class, () -> orderService.scheduleOrder(order.getId(), delay, periodical));

        Product product = productService.findAll().get(0);
        orderService.addProducts(order.getId(), product.getId(), 5);

        Order order2 = orderService.scheduleOrder(order.getId(), delay, periodical);
        assertEquals(order, order2);
        assertEquals(order.getStatus(), OrderStatus.SCHEDULED);
        assertNotNull(order.getControlDate());
        assertEquals(order.isPeriodical(), periodical);
        assertEquals(order.getDelay(), delay);
    }
}
