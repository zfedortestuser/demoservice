package com.example.demoservice.user;

import java.util.List;

/**
 * Сервис для работы с пользователями
 */
public interface UserService {
    /**
     * Найти всех пользователей
     *
     * @return список пользователей
     */
    List<User> findAll();

    /**
     * Найти пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь
     */
    User findById(long id);
}
