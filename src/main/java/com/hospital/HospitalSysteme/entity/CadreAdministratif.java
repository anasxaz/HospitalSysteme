package com.hospital.HospitalSysteme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cadre_administratifs")
@DiscriminatorValue("CADRE_ADMINISTRATIF")
public class CadreAdministratif extends Personnel{

    @Column(length = 100, nullable = false)
    private String fonction;

    // CHANGEZ EN STRING
    @Column(length = 50, nullable = false)
    private String niveauResponsabilite;

    // Relation avec Facture
    @OneToMany(mappedBy = "cadreAdministratif", cascade = CascadeType.ALL)
    private List<Facture> factures = new ArrayList<>();

    // Relation avec Planning
    @ManyToMany(mappedBy = "cadreAdministratifs")   // Notez le pluriel
    private Set<Planning> plannings = new HashSet<>();

    // Relation avec Commande
    @OneToMany(mappedBy = "cadreAdministratif", cascade = CascadeType.ALL)
    private List<Commande> commandes = new ArrayList<>();




}
