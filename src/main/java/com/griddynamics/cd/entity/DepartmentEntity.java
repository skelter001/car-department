package com.griddynamics.cd.entity;

import com.griddynamics.cd.model.DepartmentType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "description")
    private String description;
    @Column(name = "department_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;
    @OneToMany(mappedBy = "department")
    private List<EmployeeEntity> employees;
}
