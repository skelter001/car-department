package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.EmployeeEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {
    @Query
    List<EmployeeEntity> findAllEmployeesByDepartmentId(long departmentId);
}