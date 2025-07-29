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
public class InfirmierCreationDTO extends UserCreationDTO{

    @NotBlank(message = "Le niveau de qualification est obligatoire")
    private String niveauQualification;

    @NotBlank(message = "Le numéro d'ordre est obligatoire")
    private String numeroOrdre;

    @NotNull(message = "L'ID du département est obligatoire")
    private Long departementId;

    // AJOUTEZ CE CHAMP
    @NotNull(message = "Le salaire est obligatoire")
    private BigDecimal salaire;

}
