package com.griddynamics.cd.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Car {
    Long id;
    String manufacturer;
    String model;
    String vinNumber;
    Color color;
    Long employeeId;
}
