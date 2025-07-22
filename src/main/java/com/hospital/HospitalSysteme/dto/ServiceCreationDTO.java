package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCreationDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "Le tarif est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif doit être supérieur à 0")
    private BigDecimal tarif;

    private String categorie;
    private Boolean actif = true;

}
