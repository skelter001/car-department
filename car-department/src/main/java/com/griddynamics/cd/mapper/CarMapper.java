package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import lombok.ToString;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {ColorMapper.class})
public interface CarMapper {

    Car toCarModel(CarEntity entity);

    CarEntity toCarEntity(CreateCarRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "employee", source = "employee")
    CarEntity toCarEntity(CarEntity entity, EmployeeEntity employee);
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
