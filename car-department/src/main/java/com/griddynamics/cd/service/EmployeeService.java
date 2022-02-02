package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.exception.EntityDeleteException;
import com.griddynamics.cd.mapper.EmployeeMapper;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final CarRepository carRepository;
    private final EmployeeMapper employeeMapper;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toEmployeeModel)
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeMapper.toEmployeeModel(
                employeeRepository.findById(employeeId)
                        .orElseThrow(() -> new EntityNotFoundException("Employee with " + employeeId + " id was not found"))
        );
    }

    public List<Employee> getEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findAllEmployeesByDepartmentId(departmentId).stream()
                .map(employeeMapper::toEmployeeModel)
                .collect(Collectors.toList());
    }

    public Employee saveEmployee(CreateEmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeMapper.toEmployeeEntity(employeeRequest);

        if (employeeRequest.getPhoneNumber() != null) {
            if (employeeRepository.existsByPhoneNumber(employeeRequest.getPhoneNumber())) {
                throw new EntityExistsException("Employee with " + employeeRequest.getPhoneNumber() + " phone number already exist");
            }
        }

        if (employeeRequest.getDepartmentId() != null) {
            DepartmentEntity departmentEntity = departmentRepository.findById(employeeRequest.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department with " + employeeRequest.getDepartmentId() + " id was not found"));
            employeeEntity.setDepartment(departmentEntity);
        }

        return employeeMapper.toEmployeeModel(employeeRepository.save(employeeEntity));
    }

    public Employee updateEmployee(UpdateEmployeeRequest updateEmployeeRequest, Long employeeId) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with " + employeeId + " id was not found"));

        if (updateEmployeeRequest.getPhoneNumber() != null) {
            if (employeeRepository.existsByPhoneNumberAndIdIsNot(updateEmployeeRequest.getPhoneNumber(), employeeId)) {
                throw new EntityExistsException("Employee with " + updateEmployeeRequest.getPhoneNumber() + " phone number already exist");
            }
        }

        if (updateEmployeeRequest.getDepartmentId() != null) {
            DepartmentEntity departmentEntity = departmentRepository.findById(updateEmployeeRequest.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department with " + updateEmployeeRequest.getDepartmentId() + " id was not found"));
            employeeEntity.setDepartment(departmentEntity);
        }

        return employeeMapper.toEmployeeModel(
                employeeRepository.save(
                        employeeMapper.toEmployeeEntity(updateEmployeeRequest, employeeEntity)
                )
        );
    }

    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee with " + employeeId + " id was not found");
        }

        if (!carRepository.findAllCarsByEmployeeId(employeeId).isEmpty()) {
            throw new EntityDeleteException("Unable to delete employee with id " + employeeId);
        }

        employeeRepository.deleteById(employeeId);
    }
}
