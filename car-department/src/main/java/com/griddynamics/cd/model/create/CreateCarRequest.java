package com.griddynamics.cd.model.create;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
public class CreateCarRequest {

    @Pattern(regexp = "[a-zA-Z0-9\s]+", message = "Must contain only letters, numbers or spaces")
    private String manufacturer;
    @Pattern(regexp = "[a-zA-Z0-9\s]+", message = "Must contain only letters, numbers or spaces")
    private String model;
    @Pattern(regexp = "([A-Z0-9]{17})", message = "Invalid vin number")
    private String vinNumber;
    @NotNull
    private CreateColorRequest color;

    @JsonCreator
    public CreateCarRequest(@JsonProperty("manufacturer") String manufacturer,
                            @JsonProperty("model") String model,
                            @JsonProperty("vinNumber") String vinNumber,
                            @JsonProperty("color") CreateColorRequest color) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.vinNumber = vinNumber;
        this.color = color;
    }
}
