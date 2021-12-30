package com.griddynamics.cd.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Department {
    private Long id;
    private String name;
    private String email;
    private String description;
    private DepartmentType departmentType;
    private List<Employee> employees;
}
