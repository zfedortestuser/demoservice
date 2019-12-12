package com.example.demoservice.order;

import java.util.List;

/**
 * Сервис для работы с заказами
 */
public interface OrderService {
    /**
     * Создать заказ для пользователя
     *
     * @param userId идентификатор пользователя
     * @return новый заказ
     */
    Order createOrder(long userId);

    /**
     * Найти все заказы
     *
     * @return список всех заказов
     */
    List<Order> findAll();

    /**
     * Найти заказ по идентификатору
     *
     * @param id идентификатор заказа
     * @return заказ
     */
    Order findById(long id);

    /**
     * Добавить товары в заказ
     *
     * @param orderId        идентификатор заказа
     * @param productId идентификатор товара
     * @param quantity  количество товара
     * @return обновлённый заказ
     */
    OrderLine addProducts(long orderId, long productId, int quantity);

    /**
     * Завершить заказ
     *
     * @param id идентификатор заказа
     * @return обновлённый заказ
     */
    Order finishOrder(long id);

    /**
     * Запланировать отложенное или периодическое выполнение заказа
     *
     * @param id         идентификатор заказа
     * @param delay      сек. время задержки для отложенного заказа, периодичность для периодического заказа
     * @param periodical true: периодический заказ, false: отложенный заказ
     * @return обновлённый заказ
     */
    Order scheduleOrder(long id, int delay, boolean periodical);

    /**
     * Найти и обработать периодические и отложенные заказы
     */
    void processScheduledOrders();
}
