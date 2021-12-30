package com.griddynamics.cd.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "car")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String manufacturer;
    private String model;
    private String vinNumber;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "color_id", nullable = false)
    private ColorEntity color;
}
