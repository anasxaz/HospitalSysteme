package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationCreationDTO {

    @NotNull(message = "La date est obligatoire")
    private LocalDateTime date;

    private String symptomes;
    private String diagnostic;
    private String notes;

    @NotNull(message = "L'ID du médecin est obligatoire")
    private Long medecinId;

    @NotNull(message = "L'ID du dossier médical est obligatoire")
    private Long dossierMedicalId;

    private Long rendezVousId;

}
