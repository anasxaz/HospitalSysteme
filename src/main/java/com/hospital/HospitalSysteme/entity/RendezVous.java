package com.hospital.HospitalSysteme.entity;


import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

//    @Column(nullable = false)
//    private LocalDateTime dateDebut;
//
//    @Column(nullable = false)
//    private LocalDateTime dateFin;

    @Column(nullable = false)
    private int duree;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRendezVous statut;

    @Column(nullable = false)
    private String motif;

    @Column(nullable = false)
    private String notes;


    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;


}
