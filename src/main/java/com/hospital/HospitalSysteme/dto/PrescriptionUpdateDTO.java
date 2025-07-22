package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionUpdateDTO {
    private String instructions;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long consultationId;
    private Long medecinId;
    private Long patientId;
    private List<Long> medicamentIds;
}