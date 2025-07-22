package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MedecinService {

    // GESTION DU PROFIL MÉDECIN
    MedecinDTO createMedecin(MedecinCreationDTO medecinCreationDTO);
    MedecinDTO getMedecinById(Long medecinId);
    List<MedecinDTO> getAllMedecins();
    void deleteMedecin(Long medecinId);

    // GESTION DES RENDEZ-VOUS
    List<RendezVousDTO> getMedecinRendezVous(Long medecinId);
    List<RendezVousDTO> getMedecinRendezVousAVenir(Long medecinId);
    List<RendezVousDTO> getMedecinRendezVousPasses(Long medecinId);
    List<RendezVousDTO> getMedecinRendezVousByDate(Long medecinId, LocalDate date);
    List<RendezVousDTO> getMedecinRendezVousByPatient(Long medecinId, Long patientId);

    // GESTION DES CONSULTATIONS
    List<ConsultationDTO> getMedecinConsultations(Long medecinId);
    List<ConsultationDTO> getMedecinConsultationsByPatient(Long medecinId, Long patientId);
    List<ConsultationDTO> getMedecinConsultationsByPeriode(Long medecinId, LocalDateTime debut, LocalDateTime fin);

    // GESTION DES PRESCRIPTIONS
    List<PrescriptionDTO> getMedecinPrescriptions(Long medecinId);
    List<PrescriptionDTO> getMedecinPrescriptionsByPatient(Long medecinId, Long patientId);

    // RECHERCHE ET FILTRAGE
    List<MedecinDTO> searchMedecins(String query);
    List<MedecinDTO> getMedecinsBySpecialite(String specialite);
    List<MedecinDTO> getMedecinsByDisponibilite(LocalDateTime dateHeure);

}
