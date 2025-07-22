package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.InfirmierCreationDTO;
import com.hospital.HospitalSysteme.dto.InfirmierDTO;
import com.hospital.HospitalSysteme.dto.PatientDTO;
import com.hospital.HospitalSysteme.dto.PlanDeSoinsDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface InfirmierService {

    // GESTION DU PROFIL INFIRMIER
    InfirmierDTO createInfirmier(InfirmierCreationDTO infirmierCreationDTO);
    InfirmierDTO getInfirmierById(Long infirmierId);
    List<InfirmierDTO> getAllInfirmiers();
    void deleteInfirmier(Long infirmierId);

    // GESTION DES SOINS
    List<PlanDeSoinsDTO> getInfirmierPlansDeSoins(Long infirmierId);
    List<PlanDeSoinsDTO> getInfirmierPlansDeSoinsByPatient(Long infirmierId, Long patientId);
    List<PlanDeSoinsDTO> getInfirmierPlansDeSoinsByDate(Long infirmierId, LocalDate date);

    // GESTION DES PATIENTS ASSIGNÉS
    List<PatientDTO> getInfirmierPatients(Long infirmierId);

    // RECHERCHE ET FILTRAGE
    List<InfirmierDTO> searchInfirmiers(String query);
    List<InfirmierDTO> getInfirmiersByNiveauQualification(String niveauQualification);
//    List<InfirmierDTO> getInfirmiersByDisponibilite(LocalDateTime dateHeure);

}
