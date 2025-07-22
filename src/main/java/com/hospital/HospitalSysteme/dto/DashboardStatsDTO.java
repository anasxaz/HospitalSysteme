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
public class DashboardStatsDTO {

    private Long totalPatients;
    private Long totalMedecins;
    private Long totalInfirmiers;
    private Long totalRendezVous;
    private Long rendezVousAujourdhui;
    private Long consultationsAujourdhui;
    private BigDecimal revenusJournaliers;
    private BigDecimal revenusHebdomadaires;
    private BigDecimal revenusMensuels;
    private Map<String, Long> rendezVousParStatut;
    private Map<String, Long> patientsParGroupeSanguin;
    private Map<String, Long> consultationsParMois;

}
