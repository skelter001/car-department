package com.griddynamics.cd.model.update;

import com.griddynamics.cd.model.DepartmentType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class UpdateDepartmentRequest {

    @Pattern(regexp = "^[a-zA-Z]+", message = "Must contain only letters")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    private String description;
    private DepartmentType departmentType;
}
