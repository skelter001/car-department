package com.griddynamics.cd.model;

import com.griddynamics.cd.entity.ColorEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {
    BLACK(0),
    RED(1),
    GREY(2),
    WHITE(3);


    private Integer colorValue;

    public ColorEntity toEntity() {
        return ColorEntity.valueOf(this.name());
    }
}
