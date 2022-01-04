package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.ColorEntity;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateColorRequest;
import com.griddynamics.cd.model.update.UpdateColorRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    Color toColorModel(ColorEntity entity);

    ColorEntity toColorEntity(CreateColorRequest request);

    @Mapping(target = "colorName", source = "request.colorName",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ColorEntity toColorEntity(UpdateColorRequest request, @MappingTarget ColorEntity entity);
}
