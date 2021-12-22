package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.CarEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<CarEntity, Long> {
    List<CarEntity> findAllCarsByEmployeeId(Long employeeId);
}
