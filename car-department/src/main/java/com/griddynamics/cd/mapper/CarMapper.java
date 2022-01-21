package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "employeeId",
            expression = "java(entity.getEmployee().getId())")
    Car toCarModel(CarEntity entity);

    CarEntity toCarEntity(CreateCarRequest request);

    @Mapping(target = "manufacturer", source = "request.manufacturer",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "model", source = "request.model",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "vinNumber", source = "request.vinNumber",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "color", source = "request.color",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CarEntity toCarEntity(UpdateCarRequest request, @MappingTarget CarEntity entity);
}
