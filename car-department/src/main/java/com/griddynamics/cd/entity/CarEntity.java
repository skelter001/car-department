package com.griddynamics.cd.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("car")
@Getter
@AllArgsConstructor
@Builder
public class CarEntity {
    @Id
    private Long id;
    private String manufacturer;
    private String model;
    private String vinNumber;
    private ColorEntity color;
    private Long employeeId;
}
