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
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "address")
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;
    @OneToMany(mappedBy = "employee")
    private List<CarEntity> cars;
}
