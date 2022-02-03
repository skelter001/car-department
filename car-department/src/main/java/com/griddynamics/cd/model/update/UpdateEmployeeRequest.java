package com.griddynamics.cd.model.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmployeeRequest {

    @Pattern(regexp = "^[a-zA-Z]+", message = "Invalid first name value")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z]+", message = "Invalid last name value")
    private String lastName;
    @Past(message = "Invalid birthday date")
    private LocalDate birthday;
    @Pattern(regexp = "[a-zA-Z0-9,\s\\.]+", message = "Invalid address value")
    private String address;
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid phone number value")
    private String phoneNumber;
    @Positive
    private Long departmentId;
}
