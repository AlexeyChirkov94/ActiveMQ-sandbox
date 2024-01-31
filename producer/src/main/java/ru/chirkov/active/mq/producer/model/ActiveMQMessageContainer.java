package ru.chirkov.active.mq.producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveMQMessageContainer {

    Long id;
    Class<?> clazz;
    String jsonValue;

}
