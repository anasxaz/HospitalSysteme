package com.hospital.HospitalSysteme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medicaments")
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @Column(length = 50, nullable = false)
    private String dosage;

    @Column(nullable = false)
    private String effetsSecondaires;

    @Column(nullable = false)
    private String contreIndications;

    @Column(nullable = false)
    private Boolean ordonnanceRequise = false;

    @Column(length = 50)
    private String categorie;

    @Column(length = 100)
    private String fabricant;

    // Relation avec Prescription
    @ManyToMany
    @JoinTable(
            name = "prescription_medicaments",
            joinColumns = @JoinColumn(name = "medicament_id"),
            inverseJoinColumns = @JoinColumn(name = "prescription_id")
    )
    private Set<Prescription> prescriptions = new HashSet<>();




}
