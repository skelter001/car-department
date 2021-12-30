package com.griddynamics.cd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Car {

    private Long id;
    private String manufacturer;
    private String model;
    private String vinNumber;
    private Employee employee;
    private Color color;
}
