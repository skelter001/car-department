package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.DepartmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Long> {
}
