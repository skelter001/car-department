package com.griddynamics.cd.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
public class Employee {
    Long id;
    String firstName;
    String lastName;
    LocalDate birthday;
    String address;
    String phoneNumber;
    Long departmentId;
    List<Car> cars;
}
