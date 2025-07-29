package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO {

    private Long id;
    private String numero;
    private LocalDateTime date;
    private BigDecimal montantTotal;
    private StatutPaiement statutPaiement;
    private String methodePaiement;
    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private Long cadreAdministratifId;
    private String cadreAdministratifNom;
    private String cadreAdministratifPrenom;  // ← AJOUTER CECI

}
