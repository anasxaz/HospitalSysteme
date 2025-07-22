package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutPlanning;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanningDTO {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String titre;
    private String description;
    private StatutPlanning statut;

    // Ajout d'une collection d'IDs de cadres administratifs
    private Set<Long> cadreAdministratifIds;

    // On peut conserver ces champs si on souhaite toujours afficher
    // le département auquel appartiennent les cadres administratifs
    private Long departementId;
    private String departementNom;


}
