package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.exception.NoDepartmentWithSuchIdException;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;

    public List<Department> getAllDepartments() {
        return StreamSupport.stream(departmentRepository.findAll().spliterator(), false)
                .map(this::mapDepartmentEntityToDepartmentModel)
                .collect(Collectors.toList());
    }

    public Department getDepartmentById(Long id) {
        return mapDepartmentEntityToDepartmentModel(departmentRepository.findById(id)
                .orElseThrow(() -> new NoDepartmentWithSuchIdException(id)));
    }

    public void saveDepartment(Department department) {
        departmentRepository.save(mapDepartmentModelToDepartmentEntity(department));
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    private Department mapDepartmentEntityToDepartmentModel(DepartmentEntity entity) {
        return Department.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .description(entity.getDescription())
                .departmentType(entity.getDepartmentType())
                .employees(employeeService.getAllEmployeesByDepartmentId(entity.getId()))
                .build();
    }

    private DepartmentEntity mapDepartmentModelToDepartmentEntity(Department department) {
        return DepartmentEntity.builder()
                .id(department.getId())
                .name(department.getName())
                .email(department.getEmail())
                .description(department.getDescription())
                .departmentType(department.getDepartmentType())
                .employees(department.getEmployees().stream()
                        .map(employeeService::mapEmployeeModelToEmployeeEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
