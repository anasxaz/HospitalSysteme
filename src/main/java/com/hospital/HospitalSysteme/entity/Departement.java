package com.hospital.HospitalSysteme.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departements")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @Column(length = 100, nullable = false)
    private String localisation;

    // Relation avec Personnel
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    private List<Personnel> personnels = new ArrayList<>();

}
