package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
public class CarRequest {
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Must contain only letters and numbers")
    private String manufacturer;
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Must contain only letters and numbers")
    private String model;
    @Pattern(regexp = "(\\d[A-Z]{4}\\d[A-Z]{2}\\d[A-Z]{3}\\d{5})", message = "Invalid vin number")
    private String vinNumber;
    @NotNull
    private ColorRequest color;

    @JsonCreator
    public CarRequest(@JsonProperty("manufacturer") String manufacturer,
                      @JsonProperty("model") String model,
                      @JsonProperty("vinNumber") String vinNumber,
                      @JsonProperty("color") ColorRequest color) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.vinNumber = vinNumber;
        this.color = color;
    }
}
