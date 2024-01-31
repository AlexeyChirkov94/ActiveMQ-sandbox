package ru.chirkov.active.mq.producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.chirkov.active.mq.producer.model.ActiveMQMessageContainer;
import ru.chirkov.active.mq.producer.model.ModelOne;
import ru.chirkov.active.mq.producer.model.ModelTwo;

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
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService executor;
    private final AtomicLong nextValue;
    private Future<?> future;

    public MessageSender(JmsTemplate jmsTemplate, @Value("${active.mq.queue.name}") String queueName, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.topicName = queueName;
        this.executor = Executors.newScheduledThreadPool(1);
        this.nextValue = new AtomicLong(0);
        this.objectMapper = objectMapper;
    }

    public void send(ActiveMQMessageContainer message){
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
            ActiveMQMessageContainer item = generateMessage(idOfFirstMessage + i);
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

    @SneakyThrows
    private ActiveMQMessageContainer generateMessage(@Nullable Long id) {

        if (id != null) {
            ActiveMQMessageContainer result = new ActiveMQMessageContainer();
            if (id%2 == 0){
                ModelOne model = new ModelOne(id, "Alexey", "Chirkov", id.intValue());
                result.setId(id);
                result.setClazz(ModelOne.class);
                result.setJsonValue(objectMapper.writeValueAsString(model));
            } else {
                ModelTwo model = new ModelTwo(id, "5-series", "BMW", id.intValue()*3, id.intValue()*4);
                result.setId(id);
                result.setClazz(ModelTwo.class);
                result.setJsonValue(objectMapper.writeValueAsString(model));
            }
            return result;

        } else return generateMessage(nextValue.getAndIncrement());
    }

    @PreDestroy
    private void preDestroy(){
        executor.shutdown();
    }

}
