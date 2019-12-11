package com.example.demoservice.log;

import com.example.demoservice.order.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    @Query("from Log order by id desc")
    List<Log> findAllSortedById();
}
