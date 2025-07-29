package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutCommande;
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
public class CommandeDTO {

    private Long id;
    private String reference;
    private LocalDateTime dateCommande;
    private LocalDateTime dateLivraison;
    private BigDecimal montantTotal;
    private StatutCommande statut;
    private String fournisseur;
    private Long cadreAdministratifId;
    private String cadreAdministratifNom;
    private String cadreAdministratifPrenom;   // ← AJOUTER

}
