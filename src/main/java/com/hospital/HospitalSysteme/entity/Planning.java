package com.hospital.HospitalSysteme.entity;

import com.hospital.HospitalSysteme.entity.enums.StatutPlanning;
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
@Table(name = "plannings")
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Column(length = 100, nullable = false)
    private String titre;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPlanning statut;

    // Relation avec Cadre Administratif
    @ManyToMany
    @JoinTable(
            name = "planning_cadre_admins",
            joinColumns = @JoinColumn(name = "planning_id"),
            inverseJoinColumns = @JoinColumn(name = "cadre_administratif_id")
    )
    private Set<CadreAdministratif> cadreAdministratifs = new HashSet<>();
}
