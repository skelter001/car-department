package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.CarEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<CarEntity, Long> {
    @Query()
    List<CarEntity> findAllCarsByEmployeeId(Long employeeId);
}
