package com.example.demoservice.log;

import com.example.demoservice.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

@Component
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void log(String message, Object... args) {
        String formatted = MessageFormatter.format(message, args).getMessage();
        logger.info(formatted);
        logRepository.save(new Log(formatted));
    }
}
