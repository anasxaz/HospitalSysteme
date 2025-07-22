package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedecinCreationDTO extends UserCreationDTO{

    @NotBlank(message = "La spécialité est obligatoire")
    private String specialite;

    @NotBlank(message = "Le numéro d'ordre est obligatoire")
    private String numeroOrdre;

    @NotNull(message = "Le tarif de consultation est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif doit être supérieur à 0")
    private BigDecimal tarifConsultation;

    @NotNull(message = "L'ID du département est obligatoire")
    private Long departementId;


}
