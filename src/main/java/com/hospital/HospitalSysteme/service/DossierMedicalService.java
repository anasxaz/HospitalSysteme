package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.ConsultationDTO;
import com.hospital.HospitalSysteme.dto.DossierMedicalCreationDTO;
import com.hospital.HospitalSysteme.dto.DossierMedicalDTO;
import com.hospital.HospitalSysteme.dto.DossierMedicalDetailDTO;
import com.hospital.HospitalSysteme.dto.PrescriptionDTO;

import java.util.List;

public interface DossierMedicalService {

    // Opérations CRUD de base
    DossierMedicalDTO createDossierMedical(DossierMedicalCreationDTO dossierMedicalCreationDTO);
    DossierMedicalDTO getDossierMedicalById(Long dossierMedicalId);
    DossierMedicalDetailDTO getDossierMedicalDetailById(Long dossierMedicalId);
    DossierMedicalDTO updateDossierMedical(Long dossierMedicalId, DossierMedicalCreationDTO dossierMedicalUpdateDTO);
    void deleteDossierMedical(Long dossierMedicalId);

    // Opérations spécifiques
    DossierMedicalDTO getDossierMedicalByPatientId(Long patientId);
    List<ConsultationDTO> getConsultationsByDossierMedicalId(Long dossierMedicalId);
    List<PrescriptionDTO> getPrescriptionsByDossierMedicalId(Long dossierMedicalId);

    // Statistiques et recherche
    int countDossiersMedicaux();
    List<DossierMedicalDTO> searchDossiersMedicauxByPatientNom(String nom);
    List<DossierMedicalDTO> getDossiersMedicauxByGroupeSanguin(String groupeSanguin);
    List<DossierMedicalDTO> getDossiersMedicauxWithAllergies(String allergies);
}