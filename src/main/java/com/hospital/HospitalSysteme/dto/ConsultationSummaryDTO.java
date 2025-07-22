package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationSummaryDTO {
    private Long id;
    private String dateConsultation;
    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private Long medecinId;
    private String medecinNom;
    private String medecinPrenom;
    private String diagnostic;
    private String notes;
}