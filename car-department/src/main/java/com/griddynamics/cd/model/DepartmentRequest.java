package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class DepartmentRequest {

    @Pattern(regexp = "^[a-zA-Z]+", message = "Must contain only letters")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    private String description;
    @NotNull
    private DepartmentType departmentType;

    @JsonCreator
    public DepartmentRequest(@JsonProperty("name") String name,
                             @JsonProperty("email") String email,
                             @JsonProperty("description") String description,
                             @JsonProperty("departmentType") DepartmentType departmentType) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.departmentType = departmentType;
    }
}
