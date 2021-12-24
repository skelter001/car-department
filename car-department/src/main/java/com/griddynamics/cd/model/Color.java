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
public class Color {
    @NonFinal
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String colorName;

    @JsonCreator
    public Color(@JsonProperty(value = "colorName", required = true) String colorName) {
        this.colorName = colorName;
    }
}
