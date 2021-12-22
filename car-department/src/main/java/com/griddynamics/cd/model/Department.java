package com.griddynamics.cd.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Department {
    Long id;
    String name;
    String support;
    String email;
    String description;
    List<Sale> sales;
    List<Employee> employees;
}
