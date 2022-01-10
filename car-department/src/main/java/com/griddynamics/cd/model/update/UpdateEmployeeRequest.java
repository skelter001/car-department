package com.griddynamics.cd.model.update;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UpdateEmployeeRequest {

    @Pattern(regexp = "^[a-zA-Z]+", message = "Invalid first name value")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z]+", message = "Invalid last name value")
    private String lastName;
    @Past(message = "Invalid birthday date")
    private LocalDate birthday;
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Invalid address value")
    private String address;
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid phone number value")
    private String phoneNumber;

    @JsonCreator
    public UpdateEmployeeRequest(@JsonProperty("firstName") String firstName,
                                 @JsonProperty("lastName") String lastName,
                                 @JsonProperty("address") String address,
                                 @JsonProperty("phoneNumber") String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}