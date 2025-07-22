package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DossierMedicalCreationDTO {

    private String antecedentsMedicaux;
    private String allergies;

    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;

}
