package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDTO {

    private Long id;
    private LocalDateTime date;
    private String symptomes;
    private String diagnostic;
    private String notes;
    private Long medecinId;
    private String medecinNom;
    private String medecinPrenom;
    private Long dossierMedicalId;
    private Long rendezVousId;

}
