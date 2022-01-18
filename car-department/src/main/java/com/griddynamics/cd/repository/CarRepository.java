package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<CarEntity, Long> {

    List<CarEntity> findAllCarsByEmployeeId(Long employeeId);
}
