package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.NonFinal;

@Data
@Builder
@AllArgsConstructor
public class Color {
    @NonFinal
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String colorName;

    @JsonCreator
    public Color(@JsonProperty(value = "colorName", required = true) String colorName) {
        this.colorName = colorName;
    }
}
