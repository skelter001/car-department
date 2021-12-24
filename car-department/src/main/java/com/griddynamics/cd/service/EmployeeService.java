package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.exception.NoEmployeesWithSuchIdException;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CarService carService;

    public List<Employee> getAllEmployees() {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .map(this::mapEmployeeEntityToEmployeeModel)
                .collect(Collectors.toList());
    }

    public Employee getById(Long employeeId) {
        return mapEmployeeEntityToEmployeeModel(
                employeeRepository.findById(employeeId).orElseThrow(
                        () -> new NoEmployeesWithSuchIdException(employeeId))
        );
    }

    public List<Employee> getAllEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findAllEmployeesByDepartmentId(departmentId).stream()
                .map(this::mapEmployeeEntityToEmployeeModel)
                .collect(Collectors.toList());
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(mapEmployeeModelToEmployeeEntity(employee));
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public Employee mapEmployeeEntityToEmployeeModel(EmployeeEntity entity) {
        return Employee.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthday(entity.getBirthday())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .cars(carService.getAllCarsByEmployeeId(entity.getId()))
                .departmentId(entity.getDepartmentId())
                .build();
    }

    public EmployeeEntity mapEmployeeModelToEmployeeEntity(Employee employee) {
        return EmployeeEntity.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .birthday(employee.getBirthday())
                .address(employee.getAddress())
                .phoneNumber(employee.getPhoneNumber())
                .departmentId(employee.getDepartmentId())
                .cars(carService.getAllCarsByEmployeeId(Optional.ofNullable(employee.getId())
                                .orElse(0L)).stream()
                        .map(carService::mapCarModelToCarEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
