package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientStatsDTO {
    private Long totalPatients;
    private Long nouveauxPatients;
    private Map<String, Long> patientsParGroupeSanguin;
    private Map<String, Long> patientsParTrancheAge;
    private Map<String, Long> patientsParDepartement;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double moyenneAge;
    private Long patientsAvecConsultations;
}