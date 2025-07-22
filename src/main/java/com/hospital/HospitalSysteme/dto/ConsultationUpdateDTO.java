package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationUpdateDTO {
    private LocalDateTime dateConsultation;
    private String motif;
    private String diagnostic;
    private String notes;
    private Long medecinId;
    private Long patientId;
}