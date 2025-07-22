package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartementStatsDTO {
    private Long departementId;
    private String nomDepartement;
    private Long nombreMedecins;
    private Long nombreInfirmiers;
    private Long nombrePatients;
    private Long nombreConsultations;
    private Long nombreRendezVous;
    private String nom;
    private BigDecimal revenusGeneres;
    private Double tauxOccupation;
    private Long nombrePatientsTraites;

}