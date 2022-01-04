package com.griddynamics.cd.model.update;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.griddynamics.cd.model.DepartmentType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateDepartmentRequest {

    @Pattern(regexp = "^[a-zA-Z]+", message = "Must contain only letters")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    private String description;
    private DepartmentType departmentType;

    @JsonCreator
    public UpdateDepartmentRequest(@JsonProperty("name") String name,
                                   @JsonProperty("email") String email,
                                   @JsonProperty("description") String description,
                                   @JsonProperty("departmentType") DepartmentType departmentType) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.departmentType = departmentType;
    }
}
