package com.griddynamics.cd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("department")
@AllArgsConstructor
@Getter
@Builder
public class DepartmentEntity {
    private Long id;
    private String name;
    private List<String> sales;
    private String support;
    private String email;
    private String description;
}
