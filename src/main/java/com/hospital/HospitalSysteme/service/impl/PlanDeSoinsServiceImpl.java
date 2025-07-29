package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.PlanDeSoinsCreationDTO;
import com.hospital.HospitalSysteme.dto.PlanDeSoinsDTO;
import com.hospital.HospitalSysteme.dto.PlanDeSoinsUpdateDTO;
import com.hospital.HospitalSysteme.entity.Infirmier;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.entity.PlanDeSoins;
import com.hospital.HospitalSysteme.entity.Prescription;
import com.hospital.HospitalSysteme.entity.enums.StatutPlanDeSoins;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.*;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.PlanDeSoinsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PlanDeSoinsServiceImpl implements PlanDeSoinsService {

    // Dependency Injection
    private PatientRepository patientRepository;
    private PlanDeSoinsRepository planDeSoinsRepository;
    private InfirmierRepository infirmierRepository;


    private PlanDeSoinsMapper planDeSoinsMapper;





    @Override
    public PlanDeSoinsDTO createPlanDeSoins(PlanDeSoinsCreationDTO planDeSoinsCreationDTO) {
        log.info("Création d'un nouveau plan de soins le : {}", planDeSoinsCreationDTO.getDateDebut());

        if (planDeSoinsCreationDTO.getDateFin().isBefore(planDeSoinsCreationDTO.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        // ✅ AJOUT : Récupérer les entités complètes
        Patient patient = patientRepository.findById(planDeSoinsCreationDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + planDeSoinsCreationDTO.getPatientId()));

        Infirmier infirmier = infirmierRepository.findById(planDeSoinsCreationDTO.getInfirmierId())
                .orElseThrow(() -> new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + planDeSoinsCreationDTO.getInfirmierId()));

        // Convert planDeSoinsCreationDTO to plan de soins JPA Entity
        PlanDeSoins planDeSoins = planDeSoinsMapper.toEntity(planDeSoinsCreationDTO);

        // ✅ AJOUT : Assigner les entités complètes
        planDeSoins.setPatient(patient);
        planDeSoins.setInfirmier(infirmier);

        // plan de soins JPA Entity
        PlanDeSoins savedPlanDeSoins = planDeSoinsRepository.save(planDeSoins);

        log.info("Plan De Soins crée avec succès avec l'ID : {}", savedPlanDeSoins.getId());

        // Convert savedPlanDeSoins JPA Entity into DTO object
        return planDeSoinsMapper.toDTO(savedPlanDeSoins);
    }

    @Override
    public PlanDeSoinsDTO getPlanDeSoinsById(Long planDeSoinsId) {
        log.info("Récupération du plan de soins avec l'ID : {}", planDeSoinsId);

        PlanDeSoins planDeSoins = planDeSoinsRepository.findById(planDeSoinsId).orElseThrow(
                () -> new ResourceNotFoundException("Plan de soins non trouvé avec l'ID : " + planDeSoinsId)
        );

        return planDeSoinsMapper.toDTO(planDeSoins);
    }

    @Override
    public PlanDeSoinsDTO updatePlanDeSoins(Long planDeSoinsId, PlanDeSoinsUpdateDTO planDeSoinsUpdateDTO) {
        log.info("Mise à jour du plan de soins avec l'ID : {}", planDeSoinsId);

        // Récupérer le plan de soins existant
        PlanDeSoins planDeSoins = planDeSoinsRepository.findById(planDeSoinsId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan De Soins non trouvé avec l'ID : " + planDeSoinsId));

        // Mettre à jour les informations du plan de soins
        planDeSoinsMapper.updatePlanDeSoinsFromDTO(planDeSoinsUpdateDTO, planDeSoins);

        // Sauvegarder les modifications
        PlanDeSoins updatedPlanDeSoins = planDeSoinsRepository.save(planDeSoins);

        log.info("Plan De Soins mis à jour avec succès");

        // Convertir et retourner le plan de soins mis à jour
        return planDeSoinsMapper.toDTO(updatedPlanDeSoins);
    }

    @Override
    public void deletePlanDeSoins(Long planDeSoinsId) {
        log.info("Suppression du plan de soins avec l'ID : {}", planDeSoinsId);

        if (!planDeSoinsRepository.existsById(planDeSoinsId)){
            throw new ResourceNotFoundException("Plan De Soins non trouvé avec l'ID : " + planDeSoinsId);
        }

        planDeSoinsRepository.deleteById(planDeSoinsId);

        log.info("Plan De Soins supprimé avec succès");

    }

    @Override
    public List<PlanDeSoinsDTO> getAllPlansDeSoins() {
        log.info("Récupération de tous les plans de soins");

        List<PlanDeSoins> planDeSoins = planDeSoinsRepository.findAll();

        return planDeSoins.stream()
                .map((planDS) -> planDeSoinsMapper.toDTO(planDS))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanDeSoinsDTO> getPlansDeSoinsByPatientId(Long patientId) {
        log.info("Récupération des plans de soins pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        List<PlanDeSoins> planDeSoinsList = planDeSoinsRepository.findByPatientId(patientId);

        return planDeSoinsList.stream()
                .map(planDeSoinsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanDeSoinsDTO> getPlansDeSoinsByInfirmierId(Long infirmierId) {
        log.info("Récupération des plans de soins pour l'infirmier avec l'ID : {}", infirmierId);

        // Vérifier que l'infirmier existe
        if (!infirmierRepository.existsById(infirmierId)) {
            throw new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId);
        }

        List<PlanDeSoins> planDeSoinsList = planDeSoinsRepository.findByInfirmierId(infirmierId);

        return planDeSoinsList.stream()
                .map(planDeSoinsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanDeSoinsDTO> getPlansDeSoinsByStatut(StatutPlanDeSoins statut) {
        log.info("Récupération des plans de soins avec le statut : {}", statut);

        List<PlanDeSoins> planDeSoinsList = planDeSoinsRepository.findByStatut(statut);

        return planDeSoinsList.stream()
                .map(planDeSoinsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanDeSoinsDTO> getPlansDeSoinsActifs() {
        log.info("Récupération de tous les plans de soins qui sont actifs (En cours)");

        List<PlanDeSoins> planDeSoins = planDeSoinsRepository.findActifs();

        return planDeSoins.stream()
                .map((planDS) -> planDeSoinsMapper.toDTO(planDS))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanDeSoinsDTO> getPlansDeSoinsEntreDates(LocalDate dateDebut, LocalDate dateFin) {
        log.info("Récupération des plans de soins entre le : {} et le : {}", dateDebut,dateFin);

        List<PlanDeSoins> planDeSoinsList = planDeSoinsRepository.findByDateDebutGreaterThanEqualAndDateFinLessThanEqual(dateDebut, dateFin);

        return planDeSoinsList.stream()
                .map(planDeSoinsMapper::toDTO)
                .collect(Collectors.toList());
    }


    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public PlanDeSoinsDTO changerStatutPlanDeSoins(Long planDeSoinsId, StatutPlanDeSoins nouveauStatut) {
        log.info("Changer le statut du plan de soins avec l'ID : {} vers {}", planDeSoinsId, nouveauStatut);

        // Récupérer le plan de soins
        PlanDeSoins planDeSoins = planDeSoinsRepository.findById(planDeSoinsId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan de soins non trouvé avec l'ID : " + planDeSoinsId));

        // Mettre à jour le statut
        planDeSoins.setStatut(nouveauStatut);

        // Sauvegarder les modifications
        PlanDeSoins updatedPlanDeSoins = planDeSoinsRepository.save(planDeSoins);

        log.info("Statut du plan de soins mis à jour avec succès");

        return planDeSoinsMapper.toDTO(updatedPlanDeSoins);
    }

    @Override
    public int countPlansDeSoinsByStatut(StatutPlanDeSoins statut) {
        log.info("Comptage des plans de soins pour le statut : {}", statut);

        return planDeSoinsRepository.countByStatut(statut);
    }

    @Override
    public int countPlansDeSoinsByPatientId(Long patientId) {
        log.info("Comptage des plans de soins pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        return planDeSoinsRepository.countByPatientId(patientId);
    }

    @Override
    public int countPlansDeSoinsByInfirmierId(Long infirmierId) {
        log.info("Comptage des plans de soins pour l'infirmier avec l'ID : {}", infirmierId);

        // Vérifier que l'infirmier existe
        if (!infirmierRepository.existsById(infirmierId)) {
            throw new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId);
        }

        return planDeSoinsRepository.countByInfirmierId(infirmierId);
    }
}
