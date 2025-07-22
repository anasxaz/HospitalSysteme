package com.hospital.HospitalSysteme.entity;

import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPrescription statut;

    // Relation avec Consultation
    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    // Relation avec Medicament
    @ManyToMany(mappedBy = "prescriptions")
    private Set<Medicament> medicaments = new HashSet<>();


}
