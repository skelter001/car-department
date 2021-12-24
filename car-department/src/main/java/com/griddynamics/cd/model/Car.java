package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@AllArgsConstructor
public class Car {
    @NonFinal
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String manufacturer;
    String model;
    String vinNumber;
    Color color;
    Long employeeId;

    @JsonCreator
    public Car(@JsonProperty("manufacturer") String manufacturer,
               @JsonProperty("model") String model,
               @JsonProperty("vinNumber") String vinNumber,
               @JsonProperty(value = "color", required = true) Color color,
               @JsonProperty(value = "employeeId", required = true) Long employeeId) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.vinNumber = vinNumber;
        this.color = color;
        this.employeeId = employeeId;
    }
}
