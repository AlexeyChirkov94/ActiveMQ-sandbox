package ru.chirkov.active.mq.producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelOne {

    Long id;
    String name;
    String surName;
    Integer age;

}
