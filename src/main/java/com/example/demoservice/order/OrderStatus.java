package com.example.demoservice.order;

/**
 * Состояние заказа
 */
public enum OrderStatus {
    /**
     * Создан пустой заказ, допускается добавление товаров
     */
    NEW(true, false),
    /**
     * В заказе есть товары, допускается добавление товаров, допускается завершение заказа
     */
    ACTUAL(true, true),
    /**
     * Заказ завершён(куплен), запрещено добавление товаров, запрещено завершение заказа
     */
    FINISHED(false, false),
    /**
     * Периодический или отложенный заказ
     */
    SCHEDULED(false, true);

    private final boolean canAddLine;
    private final boolean canFinish;

    OrderStatus(boolean canAddLine, boolean canFinish) {
        this.canAddLine = canAddLine;
        this.canFinish = canFinish;
    }

    public boolean isCanAddLine() {
        return canAddLine;
    }

    public boolean isCanFinish() {
        return canFinish;
    }
}
