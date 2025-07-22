package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutPlanDeSoins;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeSoinsDTO {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;
    private String instructions;
    private StatutPlanDeSoins statut;
    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private Long infirmierId;
    private String infirmierNom;
    private String infirmierPrenom;



}
