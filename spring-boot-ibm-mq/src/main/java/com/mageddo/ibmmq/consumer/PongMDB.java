package com.mageddo.ibmmq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class PongMDB {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @JmsListener(destination = "PING.EVENT")
    public void consume(Message record) throws JMSException {
        logger.info("status=pong, msg={}, record={}", new String(record.getBody(byte[].class)), record);
    }
}
