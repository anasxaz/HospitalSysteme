package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacturationSummaryDTO {

    private BigDecimal montantTotal;
    private BigDecimal montantPaye;
    private BigDecimal montantDu;  // Somme des factures en attente de paiement. Ce champ est important pour le suivi financier et la gestion des impayés.
    private Long facturesPayees;
    private Long facturesEnAttente;
    private Long facturesAnnulees;
    private Map<String, BigDecimal> revenusParService;
    private Map<String, BigDecimal> revenusParMois;

}
