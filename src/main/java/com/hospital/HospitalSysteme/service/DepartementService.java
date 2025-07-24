package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.*;

import java.util.List;

public interface DepartementService {
    // Gestion des départements
    DepartementDTO createDepartement(DepartementCreationDTO departementCreationDTO);
    DepartementDTO getDepartementById(Long departementId);
    List<DepartementDTO> getAllDepartements();
    DepartementDTO updateDepartement(Long departementId, DepartementUpdateDTO departementUpdateDTO);
    void deleteDepartement(Long departementId);

    // Gestion du personnel
    void assignerMedecinADepartement(Long medecinId, Long departementId);
    void assignerInfirmierADepartement(Long infirmierId, Long departementId);
    List<MedecinDTO> getMedecinsByDepartement(Long departementId);
    List<InfirmierDTO> getInfirmiersByDepartement(Long departementId);

    // Statistiques
    DepartementStatsDTO getDepartementStats(Long departementId);

    // Recherche
    List<DepartementDTO> searchDepartements(String query);


    /**
     * Assigne un chef de département
     * @param departementId ID du département
     * @param personnelId ID du personnel à assigner comme chef
     * @return Le département mis à jour
     */
    DepartementDTO assignerChefDepartement(Long departementId, Long personnelId);
}