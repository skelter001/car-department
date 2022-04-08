package com.griddynamics.cd.model.create;

import com.griddynamics.cd.model.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCarRequest {

    @Pattern(regexp = "[a-zA-Z0-9\s]+", message = "Must contain only letters, numbers or spaces")
    private String manufacturer;
    @Pattern(regexp = "[a-zA-Z0-9\s]+", message = "Must contain only letters, numbers or spaces")
    private String model;
    @Pattern(regexp = "([A-Z0-9]{17})", message = "Invalid vin number")
    private String vinNumber;
    @NotNull
    private Color color;
    @Positive
    private Long employeeId;
}
