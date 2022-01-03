package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.mapper.EmployeeMapper;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.EmployeeRequest;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;

    public List<Employee> getAllEmployees() {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
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

    public Employee saveEmployee(EmployeeRequest employeeRequest) {
        return employeeMapper.toEmployeeModel(
                employeeRepository.save(
                        employeeMapper.toEmployeeEntity(employeeRequest))
        );
    }

    public Employee addEmployeeToDepartmentById(Long departmentId, Long employeeId) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with " + employeeId + " id was not found"));

        EmployeeEntity updateEmployeeEntity = EmployeeEntity.builder()
                .id(employeeEntity.getId())
                .firstName(employeeEntity.getFirstName())
                .lastName(employeeEntity.getLastName())
                .birthday(employeeEntity.getBirthday())
                .address(employeeEntity.getAddress())
                .phoneNumber(employeeEntity.getPhoneNumber())
                .department(departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new EntityNotFoundException("Department with " + departmentId + " id was not found")))
                .cars(employeeEntity.getCars())
                .build();

        return employeeMapper.toEmployeeModel(employeeRepository.save(updateEmployeeEntity));
    }

    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityExistsException("Employee with " + employeeId + " id does not exist");
        }
        employeeRepository.deleteById(employeeId);
    }
}
