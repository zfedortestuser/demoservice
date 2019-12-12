package com.example.demoservice.product;

import java.util.List;

/**
 * Сервис для работы с товарами
 */
public interface ProductService {
    /**
     * Найти все товары
     *
     * @return список товаров
     */
    List<Product> findAll();

    /**
     * Найти товар по идентификатору
     *
     * @param id идентификатор товара
     * @return найденный товар
     */
    Product findById(long id);
}
