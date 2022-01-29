package com.griddynamics.cd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    private Long id;
    private String manufacturer;
    private String model;
    private String vinNumber;
    private Long employeeId;
    private Color color;
}
