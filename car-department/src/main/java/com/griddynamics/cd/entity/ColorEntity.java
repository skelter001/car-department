package com.griddynamics.cd.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "color")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ColorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String colorName;
}
