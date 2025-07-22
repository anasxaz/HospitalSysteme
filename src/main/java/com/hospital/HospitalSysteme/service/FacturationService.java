package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.FactureCreationDTO;
import com.hospital.HospitalSysteme.dto.FactureDTO;
import com.hospital.HospitalSysteme.dto.FactureDetailDTO;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface FacturationService {

    // Opérations CRUD de base
    FactureDTO createFacture(FactureCreationDTO factureCreationDTO);
    FactureDTO getFactureById(Long factureId);
    FactureDetailDTO getFactureDetailById(Long factureId);
    FactureDTO updateStatutPaiement(Long factureId, StatutPaiement statutPaiement);
    FactureDTO updateMethodePaiement(Long factureId, String methodePaiement);
    void deleteFacture(Long factureId);

    // Opérations de recherche et filtrage
    List<FactureDTO> getAllFactures();
    List<FactureDTO> getFacturesByPatientId(Long patientId);
    List<FactureDTO> getFacturesByCadreAdministratifId(Long cadreAdministratifId);
    List<FactureDTO> getFacturesByStatutPaiement(StatutPaiement statutPaiement);
    List<FactureDTO> getFacturesByMethodePaiement(String methodePaiement);
    List<FactureDTO> getFacturesByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Opérations de paiement
    FactureDTO enregistrerPaiement(Long factureId, BigDecimal montant, String methodePaiement);

    // Statistiques et rapports
    int countFacturesByStatutPaiement(StatutPaiement statutPaiement);
    BigDecimal calculateTotalRevenue();
    BigDecimal calculateRevenueByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);
    Map<String, BigDecimal> getRevenueByMethodePaiement();
    Map<String, Integer> getFactureCountByStatut();
}