package com.galaplat.comprehensive.reservation.utils;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqSendUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sentMessage(String queueName, Object object) throws AmqpException {
        rabbitTemplate.convertAndSend(queueName, object);
    }

}
