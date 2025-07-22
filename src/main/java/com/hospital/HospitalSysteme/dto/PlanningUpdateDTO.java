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
public class PlanningUpdateDTO {

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String titre;
    private String description;
    private StatutPlanning statut;


    // Ajout d'une collection d'IDs de cadres administratifs
    // pour permettre la mise à jour des cadres associés au planning
    private Set<Long> cadreAdministratifIds;
}
