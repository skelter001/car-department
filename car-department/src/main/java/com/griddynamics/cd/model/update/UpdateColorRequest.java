package com.griddynamics.cd.model.update;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateColorRequest {

    @Pattern(regexp = "[a-zA-Z]+", message = "Must contains only letters")
    @NotNull
    private String colorName;

    @JsonCreator
    public UpdateColorRequest(@JsonProperty("colorName") String colorName) {
        this.colorName = colorName;
    }
}
