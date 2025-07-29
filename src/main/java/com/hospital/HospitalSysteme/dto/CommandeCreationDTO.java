package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutCommande;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CommandeCreationDTO {

    @NotBlank(message = "La référence est obligatoire")
    private String reference;

//    @NotNull(message = "La date de commande est obligatoire")
//    private LocalDateTime dateCommande;

    private LocalDateTime dateLivraison;

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant total doit être supérieur à 0")
    private BigDecimal montantTotal;

    private StatutCommande statut;

    @NotBlank(message = "Le fournisseur est obligatoire")
    private String fournisseur;

    @NotBlank(message = "La description est obligatoire")  // ← AJOUTER
    private String description;

    @NotNull(message = "L'ID du cadre administratif est obligatoire")
    private Long cadreAdministratifId;


}
