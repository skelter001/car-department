package com.griddynamics.cd.model.update;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
public class UpdateCarRequest {

    @Pattern(regexp = "[a-zA-Z0-9\s]+", message = "Must contain only letters, numbers or spaces")
    private String manufacturer;
    @Pattern(regexp = "[a-zA-Z0-9\s]+", message = "Must contain only letters, numbers or spaces")
    private String model;
    @Pattern(regexp = "([A-Z0-9]{17})", message = "Invalid vin number")
    private String vinNumber;

    private UpdateColorRequest color;

    @JsonCreator
    public UpdateCarRequest(@JsonProperty("manufacturer") String manufacturer,
                            @JsonProperty("model") String model,
                            @JsonProperty("vinNumber") String vinNumber,
                            @JsonProperty("color") UpdateColorRequest color) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.vinNumber = vinNumber;
        this.color = color;
    }
}
