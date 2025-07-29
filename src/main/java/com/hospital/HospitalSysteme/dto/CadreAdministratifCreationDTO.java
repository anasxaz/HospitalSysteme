package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CadreAdministratifCreationDTO extends UserCreationDTO{

    @NotBlank(message = "La fonction est obligatoire")
    private String fonction;

    @NotBlank(message = "Le niveau de responsabilité est obligatoire")
    private String niveauResponsabilite;

    @NotNull(message = "L'ID du département est obligatoire")
    private Long departementId;

    // AJOUTEZ CE CHAMP
    @NotNull(message = "Le salaire est obligatoire")
    private BigDecimal salaire;

}
