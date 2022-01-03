package com.griddynamics.cd.service;

import com.griddynamics.cd.mapper.DepartmentMapper;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.DepartmentRequest;
import com.griddynamics.cd.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
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

    public Department saveDepartment(DepartmentRequest departmentRequest) {
        return departmentMapper.toDepartmentModel(
                departmentRepository.save(
                        departmentMapper.toDepartmentEntity(departmentRequest))
        );
    }

    public void deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityExistsException("Department with " + departmentId + " id does not exist");
        }
        departmentRepository.deleteById(departmentId);
    }
}
