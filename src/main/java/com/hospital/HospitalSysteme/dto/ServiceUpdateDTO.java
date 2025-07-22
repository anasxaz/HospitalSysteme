package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUpdateDTO {
    private String nom;
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif doit être supérieur à 0")
    private BigDecimal tarif;

    private String categorie;
    private Boolean actif;
}