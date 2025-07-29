package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;

import java.time.LocalDate;
import java.util.List;

public interface CadreAdministratifService {
    // Gestion du profil cadre administratif
    CadreAdministratifDTO createCadreAdministratif(CadreAdministratifCreationDTO cadreAdministratifCreationDTO);
    CadreAdministratifDTO getCadreAdministratifById(Long cadreAdministratifId);
    List<CadreAdministratifDTO> getAllCadresAdministratifs();
    void deleteCadreAdministratif(Long cadreAdministratifId);

    // Gestion des rendez-vous
    RendezVousDTO createRendezVous(RendezVousCreationDTO rendezVousCreationDTO);
//    void updateRendezVousStatut(Long rendezVousId, String statut);
    void updateRendezVousStatut(Long rendezVousId, StatutRendezVous statut);
    List<RendezVousDTO> getRendezVousByDate(LocalDate date);

    // Gestion des factures
    FactureDTO createFacture(FactureCreationDTO factureCreationDTO);
    void updateFactureStatut(Long factureId, String statut);
    List<FactureDTO> getFacturesByStatut(StatutPaiement statut);

    // Gestion des départements
    void assignerCadreAdministratifADepartement(Long cadreAdministratifId, Long departementId);

    // Recherche et filtrage
    List<CadreAdministratifDTO> searchCadresAdministratifs(String query);
}