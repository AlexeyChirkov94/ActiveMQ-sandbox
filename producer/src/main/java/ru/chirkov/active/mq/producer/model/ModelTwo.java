package ru.chirkov.active.mq.producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelTwo {

    Long id;
    String model;
    String brand;
    Integer power;
    Integer speedLimit;

}
