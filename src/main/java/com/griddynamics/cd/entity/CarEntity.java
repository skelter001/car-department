package com.griddynamics.cd.entity;

import com.griddynamics.cd.model.Color;
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
    @Column(name = "id")
    private Long id;
    @Column(name = "manufacturer")
    private String manufacturer;
    @Column(name = "model")
    private String model;
    @Column(name = "vin_number")
    private String vinNumber;
    @Column(name = "color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
}
