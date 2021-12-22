package com.griddynamics.cd.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Color {
    Long id;
    String colorName;
}
