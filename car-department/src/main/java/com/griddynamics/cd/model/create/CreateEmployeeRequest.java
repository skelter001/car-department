package com.griddynamics.cd.model.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmployeeRequest {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+", message = "Invalid first name value")
    private String firstName;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+", message = "Invalid last name value")
    private String lastName;
    @Past(message = "Invalid birthday date")
    private LocalDate birthday;
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Invalid address value")
    private String address;
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid phone number value")
    private String phoneNumber;
}
