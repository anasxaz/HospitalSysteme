package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface ConsultationService {
    // Gestion des consultations
    ConsultationDTO createConsultation(ConsultationCreationDTO consultationCreationDTO);
    ConsultationDTO getConsultationById(Long consultationId);
    List<ConsultationDTO> getAllConsultations();
    ConsultationDTO updateConsultation(Long consultationId, ConsultationUpdateDTO consultationUpdateDTO);
    void deleteConsultation(Long consultationId);

    // Recherche et filtrage
    List<ConsultationDTO> getConsultationsByPatient(Long patientId);
    List<ConsultationDTO> getConsultationsByMedecin(Long medecinId);
    List<ConsultationDTO> getConsultationsByDate(LocalDate date);

    // Gestion des dossiers médicaux
    void ajouterNoteConsultation(Long consultationId, String note);
    void ajouterDiagnostic(Long consultationId, String diagnostic);

    // Relations avec d'autres entités
    List<PrescriptionDTO> getPrescriptionsByConsultation(Long consultationId);

    // Statistiques
    int countConsultationsByMedecin(Long medecinId);
    int countConsultationsByPatient(Long patientId);
    List<ConsultationSummaryDTO> getConsultationsSummaryByPatient(Long patientId);
}