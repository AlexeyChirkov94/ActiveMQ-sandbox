package ru.chirkov.active.mq.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.chirkov.active.mq.producer.model.ActiveMQMessageContainer;
import ru.chirkov.active.mq.producer.model.ModelOne;
import ru.chirkov.active.mq.producer.model.ModelTwo;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener {

    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${active.mq.queue.name}")
    public void listen(ActiveMQMessageContainer message) throws JsonProcessingException {
        if (message.getClazz() == null){
            log.info("Type of message not present");
        } else if (message.getClazz() == ModelOne.class){
            ModelOne model = objectMapper.readValue(message.getJsonValue(), ModelOne.class);
            log.info("received obj type 1 = " + model);
        } else if (message.getClazz() == ModelTwo.class){
            ModelTwo model = objectMapper.readValue(message.getJsonValue(), ModelTwo.class);
            log.info("received obj type 2 = " + model);
        }
    }

}
