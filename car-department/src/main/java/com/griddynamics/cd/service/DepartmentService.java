package com.griddynamics.cd.service;

import com.griddynamics.cd.mapper.DepartmentMapper;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import com.griddynamics.cd.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeService employeeService;

    public List<Department> getAllDepartments() {
        return StreamSupport.stream(departmentRepository.findAll().spliterator(), false)
                .map(departmentMapper::toDepartmentModel)
                .collect(Collectors.toList());
    }

    public Department getDepartmentById(Long departmentId) {
        return departmentMapper.toDepartmentModel(
                departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new EntityNotFoundException("Department with " + departmentId + " id was not found"))
        );
    }

    public Department saveDepartment(CreateDepartmentRequest createDepartmentRequest) {
        return departmentMapper.toDepartmentModel(
                departmentRepository.save(
                        departmentMapper.toDepartmentEntity(createDepartmentRequest))
        );
    }

    public void deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException("Department with " + departmentId + " id was not found");
        }
        departmentRepository.deleteById(departmentId);
    }
}
