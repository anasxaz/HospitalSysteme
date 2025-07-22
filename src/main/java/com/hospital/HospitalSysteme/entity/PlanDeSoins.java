package com.hospital.HospitalSysteme.entity;

import com.hospital.HospitalSysteme.entity.enums.StatutPlanDeSoins;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan_de_soins")
public class PlanDeSoins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPlanDeSoins statut;


    @CreationTimestamp
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateMiseAJour;


    // Relation avec Infirmier
    @ManyToOne
    @JoinColumn(name = "infirmier_id", nullable = false)
    private Infirmier infirmier;

    // Relation avec Patient
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;


}
