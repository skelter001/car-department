package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.ColorEntity;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.ColorRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    Color toColorModel(ColorEntity entity);

    ColorEntity toColorEntity(ColorRequest request);
}
