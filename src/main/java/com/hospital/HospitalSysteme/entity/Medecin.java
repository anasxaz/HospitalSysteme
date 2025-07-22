package com.hospital.HospitalSysteme.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medecins")
@DiscriminatorValue("MEDECIN")  // Héritage
public class Medecin extends Personnel{

    @Column(length = 100, nullable = false)
    private String specialite;

    @Column(length = 50, nullable = false)
    private String numeroLicence;

    private BigDecimal tarifConsultation;

    // Relation avec RDV
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    private List<RendezVous> rendezVous = new ArrayList<>();

    //Relation avec Consultation
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    private List<Consultation> consultations = new ArrayList<>();

}
