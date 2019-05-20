package com.mageddo.ibmmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@EnableJms
@EnableScheduling
@SpringBootApplication
public class Application {

    private final JmsTemplate jmsTemplate;

    public Application(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(fixedDelay = 2000)
    public void ping(){
        jmsTemplate.convertAndSend("PING.EVENT", LocalDateTime.now().toString().getBytes());
    }
}
