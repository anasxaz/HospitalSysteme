package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactureCreationDTO {

    @NotNull(message = "La date est obligatoire")
    private LocalDateTime date;

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à 0")
    private BigDecimal montantTotal;

    private StatutPaiement statutPaiement;
    private String methodePaiement;

    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;

    @NotNull(message = "L'ID du cadre administratif est obligatoire")
    private Long cadreAdministratifId;

    @NotEmpty(message = "La liste des services est obligatoire")
    private List<Long> serviceIds;

}
