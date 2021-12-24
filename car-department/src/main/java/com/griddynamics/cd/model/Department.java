package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class Department {
    @NonFinal
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    String support;
    String email;
    String description;
    List<Sale> sales;
    List<Employee> employees;

    @JsonCreator
    public Department(@JsonProperty("name") String name,
                      @JsonProperty("support") String support,
                      @JsonProperty("email") String email,
                      @JsonProperty("description") String description,
                      @JsonProperty("sales") List<Sale> sales,
                      @JsonProperty("employees") List<Employee> employees) {
        this.name = name;
        this.support = support;
        this.email = email;
        this.description = description;
        this.sales = sales;
        this.employees = employees;
    }
}
