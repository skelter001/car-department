package com.griddynamics.cd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Color {

    private Long id;
    private String colorName;
}
