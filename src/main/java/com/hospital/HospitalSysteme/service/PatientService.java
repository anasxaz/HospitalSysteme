package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PatientService {

    // Opérations CRUD de base
    // GESTION DU PROFIL PATIENT
    // Créer un nouveau patient
    PatientDTO createPatient(PatientCreationDTO patientCreationDTO);
    // Consulter les infos d'un patient
    PatientDTO getPatientById(Long patientId);
    // Récupérer tous les patients
    List<PatientDTO> getAllPatients();
    // Consulter les infos détaillées d'un patient
    PatientDetailDTO getPatientDetailsById(Long patientId);
    // Mettre à jour les infos d'un patient
    PatientDTO updatePatient(Long patientId, PatientUpdateDTO patientUpdateDTO);
    //Supprimer un patient
    void deletePatient(Long patientId);


    // Opérations spécifiques
    // GESTION DU DOSSIER MEDICAL
    // Consulter son dossier médical
    DossierMedicalDTO getPatientDossierMedical(Long patientId);
    // Obtenir un résumé médical (antécédents, allergies, etc...)
    PatientMedicalSummaryDTO getPatientMedicalSummary(Long patientId);


    //GESTION DES RENDEZ-VOUS
    // Consulter ses rendez-vous
    List<RendezVousDTO> getPatientRendezVous(Long patientId);
    // Filtrer les rendez-vous (passés, à venir, par médecin)
    List<RendezVousDTO> getPatientRendezVousAVenir(Long patientId);
    List<RendezVousDTO> getPatientRendezVousPasses(Long patientId);
    List<RendezVousDTO> getPatientRendezVousByMedecin(Long patientId, Long medecinId);


    // GESTION DES FACTURES
    // Consulter ses factures
    List<FactureDTO> getPatientFactures(Long patientId);
    // Filtrer les factures (payées, en attente)
    List<FactureDTO> getPatientFacturesByStatut(Long patientId, StatutPaiement statutPaiement);


    // RECHERCHE ET FILTRAGE
    // Rechercher des patients (pour le personnel médical)
    List<PatientDTO> searchPatients(String query);
    // Filtrer les patients par critères (groupe sanguin, etc.)
    List<PatientDTO> getPatientsByGroupeSanguin(GroupeSanguin groupeSanguin);
    List<PatientDTO> getPatientsByAllergies(String allergies);
    PatientDTO getPatientByNumeroAssurance(String numeroAssurance);




}
