package com.hospital.HospitalSysteme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personnels")
@DiscriminatorValue("PERSONNEL")
@Inheritance(strategy = InheritanceType.JOINED)  // AJOUT: nécessaire pour l'héritage
@DiscriminatorColumn(name = "type_personnel", discriminatorType = DiscriminatorType.STRING)
public abstract class Personnel extends User {

    @Column(length = 100, nullable = false)
    private String poste;

    @Column(nullable = false)
    private LocalDate dateEmbauche;

    @Column(nullable = false)
    private BigDecimal salaire;

    // Relation avec departement
    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;


}
