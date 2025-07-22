package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentCreationDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;

    @NotBlank(message = "Le dosage est obligatoire")
    private String dosage;

    private String fabricant;
    private String categorie;
    private Boolean ordonnanceRequise;

}
