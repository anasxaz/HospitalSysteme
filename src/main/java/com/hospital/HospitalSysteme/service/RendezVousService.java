package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousService {
    // Gestion des rendez-vous
    RendezVousDTO createRendezVous(RendezVousCreationDTO rendezVousCreationDTO);
    RendezVousDTO getRendezVousById(Long rendezVousId);
    List<RendezVousDTO> getAllRendezVous();
    RendezVousDTO updateRendezVous(Long rendezVousId, RendezVousUpdateDTO rendezVousUpdateDTO);
    void deleteRendezVous(Long rendezVousId);

    // Gestion des statuts
    void updateRendezVousStatut(Long rendezVousId, StatutRendezVous statut);
    List<RendezVousDTO> getRendezVousByStatut(StatutRendezVous statut);

    // Recherche et filtrage
    List<RendezVousDTO> getRendezVousByPatient(Long patientId);
    List<RendezVousDTO> getRendezVousByMedecin(Long medecinId);
    List<RendezVousDTO> getRendezVousByDate(LocalDate date);
    List<RendezVousDTO> getRendezVousBetweenDates(LocalDateTime debut, LocalDateTime fin);

    // Notifications
    void envoyerRappelRendezVous(Long rendezVousId);

    // Statistiques
    int countRendezVousByMedecinAndStatut(Long medecinId, StatutRendezVous statut);
    int countRendezVousByPatientAndStatut(Long patientId, StatutRendezVous statut);
}