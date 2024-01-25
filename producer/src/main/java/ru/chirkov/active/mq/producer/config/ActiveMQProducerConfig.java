package ru.chirkov.active.mq.producer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import ru.chirkov.active.mq.producer.model.ActiveMQMessage;

import java.util.Collections;

@Slf4j
@Configuration
@EnableJms
public class ActiveMQProducerConfig {

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTypeIdPropertyName("_type");
        converter.setTypeIdMappings(Collections.singletonMap("ActiveMQMessage", ActiveMQMessage.class));
        return converter;
    }

}
