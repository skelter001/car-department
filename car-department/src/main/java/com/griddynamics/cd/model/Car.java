package com.griddynamics.cd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Car {
    private Long id;
    private String manufacturer;
    private String model;
    private String vinNumber;
    private Color color;
    private Long employeeId;
}
