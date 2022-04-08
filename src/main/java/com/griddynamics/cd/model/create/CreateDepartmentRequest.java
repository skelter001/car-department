package com.griddynamics.cd.model.create;

import com.griddynamics.cd.model.DepartmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDepartmentRequest {

    @Pattern(regexp = "^[a-zA-Z0-9\s]+", message = "Must contain only letters")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    private String description;
    @NotNull
    private DepartmentType departmentType;
}
