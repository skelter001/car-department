package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ColorRequest {

    @Pattern(regexp = "[a-zA-Z]+", message = "Must contains only letters")
    private String colorName;

    @JsonCreator
    public ColorRequest(@JsonProperty("colorName") String colorName) {
        this.colorName = colorName;
    }
}
