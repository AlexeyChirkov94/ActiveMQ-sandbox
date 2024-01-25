package ru.chirkov.active.mq.producer.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.chirkov.active.mq.producer.model.ActiveMQMessage;
import ru.chirkov.active.mq.producer.service.MessageSender;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/producer")
@AllArgsConstructor
public class ProducerController {

    private final MessageSender messageSender;

    @PostMapping("/send/one")
    public void send(@RequestParam(required = false) Long messageId,
                     @RequestParam(required = false) String messageValue
    ) {
        messageSender.send(new ActiveMQMessage(messageId, messageValue));
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
