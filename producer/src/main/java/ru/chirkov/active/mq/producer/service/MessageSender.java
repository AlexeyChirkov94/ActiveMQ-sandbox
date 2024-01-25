package ru.chirkov.active.mq.producer.service;

import jakarta.annotation.Nullable;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.chirkov.active.mq.producer.model.ActiveMQMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final String topicName;
    private final ScheduledExecutorService executor;
    private final AtomicLong nextValue;
    private Future<?> future;

    public MessageSender(JmsTemplate jmsTemplate, @Value("${active.mq.queue.name}") String queueName) {
        this.jmsTemplate = jmsTemplate;
        this.topicName = queueName;
        this.executor = Executors.newScheduledThreadPool(1);
        this.nextValue = new AtomicLong(0);
    }

    public void send(ActiveMQMessage message){
        if (message == null) message = generateMessage(null);
        if (message.getId() == null) message.setId(nextValue.getAndIncrement());

        try{
            jmsTemplate.convertAndSend(topicName, message);
        } catch (Exception exception){
            log.error("send error, value:{}", message, exception);
        }
    }

    public void sendManyMessages(@Nullable Long idOfFirstMessage, Integer countOfMessage) {
        if (idOfFirstMessage == null) idOfFirstMessage = nextValue.getAndIncrement();
        for (int i = 0; i < countOfMessage; i++) {
            ActiveMQMessage item = generateMessage(idOfFirstMessage + i);
            send(item);
        }
    }

    public void startGenerateMessagePeriodically(@Nullable Long idOfFirstMessage, Long period, TimeUnit timeUnit){
        if (idOfFirstMessage != null) nextValue.set(idOfFirstMessage);
        future = executor.scheduleAtFixedRate(() -> send(generateMessage(null)), 0, period, timeUnit);
    }

    public void stopGenerateMessagePeriodically(){
        if (future != null) future.cancel(true);
    }

    private ActiveMQMessage generateMessage(@Nullable Long id) {
        if (id != null) return new ActiveMQMessage(id, "value = " + id);
        else return generateMessage(nextValue.getAndIncrement());
    }

    @PreDestroy
    private void preDestroy(){
        executor.shutdown();
    }

}
