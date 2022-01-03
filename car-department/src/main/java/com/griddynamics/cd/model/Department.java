package com.griddynamics.cd.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Department {

    private Long id;
    private String name;
    private String email;
    private String description;
    private DepartmentType departmentType;
}
