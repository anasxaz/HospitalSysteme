package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.PlanDeSoinsCreationDTO;
import com.hospital.HospitalSysteme.dto.PlanDeSoinsDTO;
import com.hospital.HospitalSysteme.dto.PlanDeSoinsUpdateDTO;
import com.hospital.HospitalSysteme.entity.enums.StatutPlanDeSoins;

import java.time.LocalDate;
import java.util.List;

public interface PlanDeSoinsService {

    // Opérations CRUD de base
    PlanDeSoinsDTO createPlanDeSoins(PlanDeSoinsCreationDTO planDeSoinsCreationDTO);
    PlanDeSoinsDTO getPlanDeSoinsById(Long planDeSoinsId);
    PlanDeSoinsDTO updatePlanDeSoins(Long planDeSoinsId, PlanDeSoinsUpdateDTO planDeSoinsUpdateDTO);
    void deletePlanDeSoins(Long planDeSoinsId);

    // Opérations de recherche et filtrage
    List<PlanDeSoinsDTO> getAllPlansDeSoins();
    List<PlanDeSoinsDTO> getPlansDeSoinsByPatientId(Long patientId);
    List<PlanDeSoinsDTO> getPlansDeSoinsByInfirmierId(Long infirmierId);
    List<PlanDeSoinsDTO> getPlansDeSoinsByStatut(StatutPlanDeSoins statut);
    List<PlanDeSoinsDTO> getPlansDeSoinsActifs();
    List<PlanDeSoinsDTO> getPlansDeSoinsEntreDates(LocalDate dateDebut, LocalDate dateFin);

    // Opérations de gestion du statut
    PlanDeSoinsDTO changerStatutPlanDeSoins(Long planDeSoinsId, StatutPlanDeSoins nouveauStatut);

    // Statistiques
    int countPlansDeSoinsByStatut(StatutPlanDeSoins statut);
    int countPlansDeSoinsByPatientId(Long patientId);
    int countPlansDeSoinsByInfirmierId(Long infirmierId);
}