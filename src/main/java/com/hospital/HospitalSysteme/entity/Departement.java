package com.hospital.HospitalSysteme.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private Integer capacite;   // la capacité du département est une information importante pour votre système (nombre de lits, nombre de patients pouvant être traités, etc.

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "chef_departement_id")
    private Personnel chefDepartement;

    // Relation avec Personnel
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    private List<Personnel> personnels = new ArrayList<>();

}
