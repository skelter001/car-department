package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);
}
