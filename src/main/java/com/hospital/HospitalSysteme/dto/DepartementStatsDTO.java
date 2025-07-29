package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

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

    // NOUVEAUX CHAMPS SUGGÉRÉS
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Map<String, Long> consultationsParMois;
    private Map<String, Long> rendezVousParStatut;
    private Double moyenneConsultationsParMedecin;
    private BigDecimal revenusParConsultation; // Revenus / nombre de consultations
    private Long consultationsAnnulees;
    private String specialitePrincipale; // Spécialité la plus représentée dans le département


}