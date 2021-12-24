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
    private final SaleService saleService;

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
                .support(entity.getSupport())
                .email(entity.getEmail())
                .description(entity.getDescription())
                .sales(saleService.getSalesByDepartmentId(entity.getId()))
                .employees(employeeService.getAllEmployeesByDepartmentId(entity.getId()))
                .build();
    }

    private DepartmentEntity mapDepartmentModelToDepartmentEntity(Department department) {
        return DepartmentEntity.builder()
                .id(department.getId())
                .name(department.getName())
                .sales(department.getSales().stream()
                        .map(saleService::mapModelToEntity)
                        .collect(Collectors.toList()))
                .employees(department.getEmployees().stream()
                        .map(employeeService::mapEmployeeModelToEmployeeEntity)
                        .collect(Collectors.toList()))
                .support(department.getSupport())
                .email(department.getEmail())
                .description(department.getDescription())
                .build();
    }
}
