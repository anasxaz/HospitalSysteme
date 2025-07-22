package com.hospital.HospitalSysteme.entity;

import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
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
@Table(name = "patients")
@DiscriminatorValue("PATIENT")
public class Patient extends User{

    @Column(length = 50, nullable = false, unique = true)
    private String numeroAssurance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupeSanguin groupeSanguin;

    @Column(nullable = false)
    private String allergies;

    @Column(nullable = false)
    private String antecedentsMedicaux;

    @Column(length = 100, nullable = false)
    private String contactUrgenceNom;

    @Column(length = 20, nullable = false)
    private String contactUrgenceTelephone;

    // Relation avec RDV
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<RendezVous> rendezVous = new ArrayList<>();

    // Relation avec Plan de soins
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PlanDeSoins> planDeSoins = new ArrayList<>();

    // Relation avec Dossier Medical
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private DossierMedical dossierMedical;

    // Relation avec Facture
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Facture> factures = new ArrayList<>();


}
