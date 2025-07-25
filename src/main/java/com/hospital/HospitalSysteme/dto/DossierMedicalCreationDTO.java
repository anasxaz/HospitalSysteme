package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
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
    private GroupeSanguin groupeSanguin;

    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;

}
