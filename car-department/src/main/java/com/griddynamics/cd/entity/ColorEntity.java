package com.griddynamics.cd.entity;

import com.griddynamics.cd.model.Color;
import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("color")
@AllArgsConstructor
public enum ColorEntity {
    BLACK(0),
    RED(1),
    GREY(2),
    WHITE(3);


    private Integer colorValue;

    public Color toModel() {
        return Color.valueOf(this.name());
    }
}
