package com.griddynamics.cd.controller;

import com.griddynamics.cd.annotation.NotEmptyOrNull;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.DepartmentType;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import com.griddynamics.cd.model.update.UpdateDepartmentRequest;
import com.griddynamics.cd.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/departments")
@AllArgsConstructor
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @Operation(
            summary = "Get all departments with paging and optional filtering",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public ResponseEntity<?> getAllDepartments(@NotEmptyOrNull(message = "Name list should be null or not empty")
                                               @RequestParam(required = false) List<String> names,
                                               @NotEmptyOrNull(message = "Email list should be null or not empty")
                                               @RequestParam(required = false) List<String> emails,
                                               @NotEmptyOrNull(message = "Description list should be null or not empty")
                                               @RequestParam(required = false) List<String> descriptions,
                                               @NotEmptyOrNull(message = "Department type list should be null or not empty")
                                               @RequestParam(required = false) List<DepartmentType> departmentTypes,
                                               @RequestParam(defaultValue = "0")
                                               @PositiveOrZero(message = "Page number should be positive or 0") int pageNumber,
                                               @RequestParam(defaultValue = "3")
                                               @Positive(message = "Page size should be positive") int pageSize,
                                               @RequestParam(defaultValue = "id") String orderBy,
                                               @RequestParam(defaultValue = "ASC") Sort.Direction order) {
        return departmentService.getDepartmentsWithFiltering(names, emails, descriptions, departmentTypes, pageNumber, pageSize, orderBy, order);
    }

    @GetMapping("/{departmentId}")
    @Operation(
            summary = "Get department by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Department getDepartmentById(@PathVariable Long departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    @PostMapping
    @Operation(
            summary = "Save department model",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Department saveDepartment(@RequestBody @Valid CreateDepartmentRequest departmentRequest) {
        return departmentService.saveDepartment(departmentRequest);
    }

    @PutMapping("/{departmentId}")
    @Operation(
            summary = "Update department model",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Department updateDepartment(@PathVariable Long departmentId,
                                       @RequestBody @Valid UpdateDepartmentRequest updateDepartmentRequest) {
        return departmentService.updateDepartment(updateDepartmentRequest, departmentId);
    }

    @DeleteMapping("/{departmentId}")
    @Operation(
            summary = "Delete department by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public void deleteDepartmentById(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
    }
}
