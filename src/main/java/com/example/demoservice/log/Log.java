package com.example.demoservice.log;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Log {
    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private Date date;

    public Log() {
    }

    public Log(String message) {
        this.message = message;
        this.date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
