package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface PrescriptionService {
    // Gestion des prescriptions
    PrescriptionDTO createPrescription(PrescriptionCreationDTO prescriptionCreationDTO);
    PrescriptionDTO getPrescriptionById(Long prescriptionId);
    List<PrescriptionDTO> getAllPrescriptions();
    PrescriptionDTO updatePrescription(Long prescriptionId, PrescriptionUpdateDTO prescriptionUpdateDTO);
    void deletePrescription(Long prescriptionId);

    // Recherche et filtrage
    List<PrescriptionDTO> getPrescriptionsByPatient(Long patientId);
    List<PrescriptionDTO> getPrescriptionsByMedecin(Long medecinId);
    List<PrescriptionDTO> getPrescriptionsByConsultation(Long consultationId);
    List<PrescriptionDTO> getPrescriptionsByDate(LocalDate date);

    // Gestion des médicaments
    void ajouterMedicamentAPrescription(Long prescriptionId, MedicamentDTO medicamentDTO);
    void supprimerMedicamentDePrescription(Long prescriptionId, Long medicamentId);

    // Renouvellement
    PrescriptionDTO renouvelerPrescription(Long prescriptionId, int dureeJours);

    // Statistiques
    int countPrescriptionsByMedecin(Long medecinId);
    int countPrescriptionsByPatient(Long patientId);
    List<MedicamentStatDTO> getMedicamentsLesPlusPrescrits(int limit);
}