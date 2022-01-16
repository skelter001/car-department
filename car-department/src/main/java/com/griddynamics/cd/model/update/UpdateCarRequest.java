package com.griddynamics.cd.model.update;

import com.griddynamics.cd.model.Color;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
public class UpdateCarRequest {

    @Pattern(regexp = "[a-zA-Z0-9\s]+", message = "Must contain only letters, numbers or spaces")
    private String manufacturer;
    @Pattern(regexp = "[a-zA-Z0-9\s]+", message = "Must contain only letters, numbers or spaces")
    private String model;
    @Pattern(regexp = "([A-Z0-9]{17})", message = "Invalid vin number")
    private String vinNumber;
    private Color color;
    @Positive
    private Long employeeId;
}
