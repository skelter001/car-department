package com.griddynamics.cd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class Department {
    private Long id;
    private String name;
    private List<String> sales;
    private String support;
    private String email;
    private String description;
    private List<Employee> employees;
}
