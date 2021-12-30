package com.griddynamics.cd.service;

import com.griddynamics.cd.mapper.EmployeeMapper;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.EmployeeRequest;
import com.griddynamics.cd.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final CarService carService;

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

    public List<Employee> getAllEmployeesByDepartmentId(Long departmentId) {
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

    public Employee addCarToEmployeeById(Long employeeId, Long carId) {
        Employee employee = getEmployeeById(employeeId);

        employee.getCars().add(carService.getCarById(carId));

        return employeeMapper.toEmployeeModel(
                employeeRepository.save(
                        employeeMapper.toEmployeeEntity(employee))
        );
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}
