package com.griddynamics.cd.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String address;
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private List<CarEntity> cars;
}
