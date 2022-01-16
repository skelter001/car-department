package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findAllEmployeesByDepartmentId(Long departmentId);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdIsNot(String phoneNumber, Long id);
}
