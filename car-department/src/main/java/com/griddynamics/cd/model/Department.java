package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Department {
    @NonFinal
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    private String description;
    private DepartmentType departmentType;
    private List<Employee> employees;

    @JsonCreator
    public Department(@JsonProperty("name") String name,
                      @JsonProperty("email") String email,
                      @JsonProperty("description") String description,
                      @JsonProperty("departmentType") DepartmentType departmentType,
                      @JsonProperty("employees") List<Employee> employees) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.departmentType = departmentType;
        this.employees = employees;
    }
}
