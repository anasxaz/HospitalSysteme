package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.CommandeCreationDTO;
import com.hospital.HospitalSysteme.dto.CommandeDTO;
import com.hospital.HospitalSysteme.dto.CommandeUpdateDTO;
import com.hospital.HospitalSysteme.entity.enums.StatutCommande;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CommandeService {

    // Opérations CRUD de base
    CommandeDTO createCommande(CommandeCreationDTO commandeCreationDTO);
    CommandeDTO getCommandeById(Long commandeId);
    CommandeDTO updateCommande(Long commandeId, CommandeUpdateDTO commandeUpdateDTO);
    void deleteCommande(Long commandeId);

    // Opérations de recherche et filtrage
    List<CommandeDTO> getAllCommandes();
    List<CommandeDTO> getCommandesByStatut(StatutCommande statut);
    List<CommandeDTO> getCommandesByCadreAdministratif(Long cadreAdministratifId);
    List<CommandeDTO> getCommandesByFournisseur(String fournisseur);
    List<CommandeDTO> getCommandesByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Opérations de gestion
    CommandeDTO changerStatutCommande(Long commandeId, StatutCommande nouveauStatut);
//    CommandeDTO enregistrerLivraison(Long commandeId, LocalDateTime dateLivraison);
    CommandeDTO enregistrerLivraison(Long commandeId);
    CommandeDTO annulerCommande(Long commandeId);

    // Statistiques
    int countCommandesByStatut(StatutCommande statut);
    BigDecimal calculateTotalMontantByStatut(StatutCommande statut);
    BigDecimal calculateTotalMontantByFournisseur(String fournisseur);
    BigDecimal calculateTotalMontantByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);
    Map<StatutCommande, Integer> countCommandesByAllStatuts();
    Map<String, BigDecimal> calculateTotalMontantByAllFournisseurs();
    List<CommandeDTO> getTopFournisseursByMontant(int limit);
}