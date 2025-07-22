package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedicalSummaryDTO {

    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private LocalDate dateNaissance;
    private GroupeSanguin groupeSanguin;
    private String allergies;
    private String antecedentsMedicaux;
    private List<ConsultationDTO> dernieresConsultations;
    private List<PrescriptionDTO> prescriptionActives;
    private List<PlanDeSoinsDTO> planDeSoinsActifs;

}
