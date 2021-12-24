package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class Employee {
    @NonFinal
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String firstName;
    String lastName;
    LocalDate birthday;
    String address;
    String phoneNumber;
    Long departmentId;
    List<Car> cars;

    @JsonCreator
    public Employee(@JsonProperty("firstName") String firstName,
                    @JsonProperty("lastName") String lastName,
                    @JsonProperty("birthday") LocalDate birthday,
                    @JsonProperty("address") String address,
                    @JsonProperty("phoneNumber") String phoneNumber,
                    @JsonProperty(value = "departmentId", required = true) Long departmentId,
                    @JsonProperty("cars") List<Car> cars) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.departmentId = departmentId;
        this.cars = cars;
    }
}
