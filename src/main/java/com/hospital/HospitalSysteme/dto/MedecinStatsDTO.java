package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedecinStatsDTO {
    private Long medecinId;
    private String nom;
    private String prenom;
    private String specialite;
    private Long totalConsultations;
    private Long consultationsEnAttente;
    private Long consultationsTerminees;
    private BigDecimal revenusGeneres;
    private Double tauxSatisfactionPatients;
    private Map<String, Long> consultationsParJourSemaine;
    private Map<String, Long> patientsParGroupeAge;
}