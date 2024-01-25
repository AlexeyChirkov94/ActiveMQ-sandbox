package ru.chirkov.active.mq.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.chirkov.active.mq.consumer.model.ActiveMQMessage;

@Slf4j
@Service
public class MessageListener {

    @JmsListener(destination = "${active.mq.queue.name}")
    public void listen(ActiveMQMessage message) {
        log.info("received obj = " + message);
    }

}
