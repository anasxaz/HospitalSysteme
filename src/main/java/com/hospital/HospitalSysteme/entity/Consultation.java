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
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String symptomes;

    @Column(nullable = false)
    private String diagnostic;

    @Column(nullable = false, columnDefinition = "TEXT")  // ✅ AJOUTÉ TEXT pour éviter la troncature
    private String notes;

    @Column(nullable = false, columnDefinition = "TEXT")  // ✅ AJOUTÉ TEXT
    private String recommandations;

    // Relation avec Medecin
    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;

    // Relation avec Dossier Medical
    @ManyToOne
    @JoinColumn(name = "dossier_medical_id", nullable = false)
    private DossierMedical dossierMedical;

    // ✅ AJOUTÉ : Relation avec RendezVous (manquait dans votre entité !)
    @ManyToOne
    @JoinColumn(name = "rendez_vous_id")
    private RendezVous rendezVous;

    // Relation avec Prescription
    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions = new ArrayList<>();

}
