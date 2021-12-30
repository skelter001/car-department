package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.CarRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {
    Car toCarModel(CarEntity entity);
    CarEntity toCarEntity(CarRequest request);
    CarEntity toCarEntity(Car car);
}
