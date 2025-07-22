package com.hospital.HospitalSysteme.entity;

import com.hospital.HospitalSysteme.entity.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private LocalDateTime dateCommande;

    @Column(nullable = false)
    private LocalDateTime dateLivraison;

    @Column(length = 100, nullable = false)
    private String fournisseur;

    @Column(nullable = false)
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCommande statut;

    @Column(nullable = false)
    private String description;

    // Relation avec Cadre Administratif
    @ManyToOne
    @JoinColumn(name = "cadre_administratif_id", nullable = false)
    private CadreAdministratif cadreAdministratif;




}
