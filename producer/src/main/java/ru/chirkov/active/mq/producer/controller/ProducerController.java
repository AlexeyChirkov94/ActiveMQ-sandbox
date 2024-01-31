package ru.chirkov.active.mq.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.chirkov.active.mq.producer.model.ActiveMQMessageContainer;
import ru.chirkov.active.mq.producer.model.ModelOne;
import ru.chirkov.active.mq.producer.model.ModelTwo;
import ru.chirkov.active.mq.producer.service.MessageSender;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/producer")
@AllArgsConstructor
public class ProducerController {

    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;

    @PostMapping("/send/model/one")
    public void sendModelOne(@RequestParam(required = false, defaultValue = "0") Long messageId,
                     @RequestParam(required = false) String name,
                     @RequestParam(required = false) String surname
    ) throws JsonProcessingException {

        ActiveMQMessageContainer result = new ActiveMQMessageContainer();
        ModelOne model = new ModelOne(messageId*2, name, surname, messageId.intValue());
        result.setId(messageId);
        result.setClazz(ModelOne.class);
        result.setJsonValue(objectMapper.writeValueAsString(model));

        messageSender.send(result);
    }

    @PostMapping("/send/model/two")
    public void sendModelTwo(@RequestParam(required = false) Long messageId,
                        @RequestParam(required = false) String model,
                        @RequestParam(required = false) String brand,
                        @RequestParam(required = false) Integer power,
                        @RequestParam(required = false) Integer speedLimit
    ) throws JsonProcessingException {
        ActiveMQMessageContainer result = new ActiveMQMessageContainer();
        ModelTwo model1 = new ModelTwo(messageId*2, model, brand, power, speedLimit);
        result.setId(messageId);
        result.setClazz(ModelTwo.class);
        result.setJsonValue(objectMapper.writeValueAsString(model1));

        messageSender.send(result);
    }

    @PostMapping("/send/many")
    public void sendManyMessages(@RequestParam(required = false) Long firstMessageId,
                                 @RequestParam Integer countOfMessages
    ) {
        messageSender.sendManyMessages(firstMessageId, countOfMessages);
    }

    @PostMapping("/send/periodically/start")
    public void startGenerateMessagePeriodically(@RequestParam(required = false) Long firstMessageId,
                                                 @RequestParam Long period,
                                                 @RequestParam(defaultValue = "SECONDS") TimeUnit timeUnit
    ) {
        messageSender.startGenerateMessagePeriodically(firstMessageId, period, timeUnit);
    }

    @PostMapping("/send/periodically/stop")
    public void stopGenerateMessagePeriodically() {
        messageSender.stopGenerateMessagePeriodically();
    }

}
