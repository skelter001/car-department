package com.griddynamics.cd.controller;

import com.griddynamics.cd.annotation.NotEmptyOrNull;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import com.griddynamics.cd.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(value = "/employees")
    @Operation(
            summary = "Get all employees with paging and optional filtering",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public ResponseEntity<?> getAllEmployees(@NotEmptyOrNull(message = "First name list should be null or not empty")
                                             @RequestParam(required = false) List<String> firstNames,
                                             @NotEmptyOrNull(message = "Last name list should be null or not empty")
                                             @RequestParam(required = false) List<String> lastNames,
                                             @NotEmptyOrNull(message = "Birthday list should be null or not empty")
                                             @RequestParam(required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> birthdays,
                                             @NotEmptyOrNull(message = "Address list should be null or not empty")
                                             @RequestParam(required = false) List<String> addresses,
                                             @NotEmptyOrNull(message = "Phone number list should be null or not empty")
                                             @RequestParam(required = false) List<String> phoneNumbers,
                                             @NotEmptyOrNull(message = "Department id list should be null or not empty")
                                             @RequestParam(required = false) List<Long> departmentIds,
                                             @RequestParam(defaultValue = "0")
                                             @PositiveOrZero(message = "Page number should be positive or 0") int pageNumber,
                                             @RequestParam(defaultValue = "3")
                                             @Positive(message = "Page size should be positive") int pageSize,
                                             @RequestParam(defaultValue = "id") String orderBy,
                                             @RequestParam(defaultValue = "ASC") Sort.Direction order) {
        return employeeService.getEmployeesWithFiltering(firstNames, lastNames, birthdays, addresses, phoneNumbers, departmentIds, pageNumber, pageSize, orderBy, order);
    }

    @GetMapping("/employees/{employeeId}")
    @Operation(
            summary = "Get employee by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @GetMapping("/departments/{departmentId}/employees")
    @Operation(
            summary = "Get all employees from specific department by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public List<Employee> getEmployeesByDepartmentId(@PathVariable Long departmentId) {
        return employeeService.getEmployeesByDepartmentId(departmentId);
    }

    @PostMapping("/employees")
    @Operation(
            summary = "Save employee model",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Employee saveEmployee(@RequestBody @Valid CreateEmployeeRequest employeeRequest) {
        return employeeService.saveEmployee(employeeRequest);
    }

    @PutMapping("/employees/{employeeId}")
    @Operation(
            summary = "Update employee model",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Employee updateEmployee(@PathVariable Long employeeId,
                                   @RequestBody @Valid UpdateEmployeeRequest updateEmployeeRequest) {
        return employeeService.updateEmployee(updateEmployeeRequest, employeeId);
    }

    @DeleteMapping("/employees/{employeeId}")
    @Operation(
            summary = "Delete employee by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public void deleteEmployeeById(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
    }
}
