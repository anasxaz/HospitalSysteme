package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.*;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.*;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.CadreAdministratifService;
import com.hospital.HospitalSysteme.service.DepartementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j

public class DepartementServiceImpl implements DepartementService {


    // Dependency Injection
    private final RendezVousRepository rendezVousRepository;
    private final CadreAdministratifRepository cadreAdministratifRepository;
    private final DepartementRepository departementRepository;
    private final FactureRepository factureRepository;
    private final MedecinRepository medecinRepository;
    private final InfirmierRepository infirmierRepository;
    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final PersonnelRepository personnelRepository;



    private final RendezVousMapper rendezVousMapper;
    private final CadreAdministratifMapper cadreAdministratifMapper;
    private final FactureMapper factureMapper;
    private final DepartementMapper departementMapper;
    private final MedecinMapper medecinMapper;
    private final InfirmierMapper infirmierMapper;



    @Override
    public DepartementDTO createDepartement(DepartementCreationDTO departementCreationDTO) {
        log.info("Création d'un nouveau departement ");

        // Vérifier si le département existe déjà
        if (departementRepository.existsByNom(departementCreationDTO.getNom())) {
            throw new IllegalArgumentException("Un département avec ce nom existe déjà : " + departementCreationDTO.getNom());
        }

        // Convert departementCreationDTO to departement JPA Entity
        Departement departement = departementMapper.toEntity(departementCreationDTO);

        // cadreAdministratif JPA Entity
        Departement savedDepartement = departementRepository.save(departement);

        log.info("Département créé avec succès avec l'ID : {}", savedDepartement.getId());

        // Convert saved cadre administratif JPA Entity into DTO object
        return departementMapper.toDTO(savedDepartement);
    }

    @Override
    public DepartementDTO getDepartementById(Long departementId) {
        log.info("Récupération du département avec l'ID : {}", departementId);

        Departement departement = departementRepository.findById(departementId).orElseThrow(
                () -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId)
        );

        return departementMapper.toDTO(departement);
    }

    @Override
    public List<DepartementDTO> getAllDepartements() {
        log.info("Récupération de tous les départements");

        List<Departement> departements = departementRepository.findAll();

        return departements.stream()
                .map(departementMapper::toDTO)
                .collect(Collectors.toList());
    }

//    @Override
//    public DepartementDTO updateDepartement(Long departementId, DepartementUpdateDTO departementUpdateDTO) {
//        log.info("Mise à jour du département avec l'ID : {}", departementId);
//
//        // Retrieve the département
//        Departement departement = departementRepository.findById(departementId).orElseThrow(
//                () -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId)
//        );
//
//        // Mettre à jour les infos
//        departementMapper.updateDepartementFromDTO(departementUpdateDTO, departement);
//
//        // Sauvegarder l'update
//        Departement departementUpdated = departementRepository.save(departement);
//
//        log.info("Département mis à jour avec succès");
//
//        return departementMapper.toDTO(departementUpdated);
//    }
@Override
public DepartementDTO updateDepartement(Long departementId, DepartementUpdateDTO departementUpdateDTO) {
    log.info("Mise à jour du département avec l'ID : {}", departementId);

    // Retrieve the département
    Departement departement = departementRepository.findById(departementId).orElseThrow(
            () -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId)
    );

    // Mettre à jour les infos de base via le mapper
    departementMapper.updateDepartementFromDTO(departementUpdateDTO, departement);

    // Gérer manuellement le chef de département
    if (departementUpdateDTO.getChefDepartementId() != null) {
        if (departementUpdateDTO.getChefDepartementId() > 0) {
            // Rechercher le personnel qui sera chef de département
            Personnel personnel = personnelRepository.findById(departementUpdateDTO.getChefDepartementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Personnel non trouvé avec l'ID : " +
                            departementUpdateDTO.getChefDepartementId()));
            departement.setChefDepartement(personnel);
        } else {
            // Si l'ID est 0, on retire le chef de département
            departement.setChefDepartement(null);
        }
    }

    // Sauvegarder l'update
    Departement departementUpdated = departementRepository.save(departement);

    log.info("Département mis à jour avec succès");

    return departementMapper.toDTO(departementUpdated);
}

    @Override
    public void deleteDepartement(Long departementId) {
        log.info("Suppression du département avec l'ID : {}", departementId);

        // vérifier l'existance du département
        if (!departementRepository.existsById(departementId)){
            throw new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId);
        }

        departementRepository.deleteById(departementId);

        log.info("Département supprimé avec succès");

    }

    @Override
    public void assignerMedecinADepartement(Long medecinId, Long departementId) {
        log.info("Assignation du médecin avec l'ID : {} au département avec l'ID : {}", medecinId, departementId);

        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId));

        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId));

        medecin.setDepartement(departement);
        medecinRepository.save(medecin);

        log.info("Médecin assigné au département avec succès");
    }

    @Override
    public void assignerInfirmierADepartement(Long infirmierId, Long departementId) {
        log.info("Assignation de l'infirmier avec l'ID : {} au département avec l'ID : {}", infirmierId, departementId);

        Infirmier infirmier = infirmierRepository.findById(infirmierId)
                .orElseThrow(() -> new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId));

        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId));

        infirmier.setDepartement(departement);
        infirmierRepository.save(infirmier);

        log.info("Infirmier assigné au département avec succès");
    }

    @Override
    public List<MedecinDTO> getMedecinsByDepartement(Long departementId) {
        log.info("Récupération des médecins du département avec l'ID : {}", departementId);

        // Vérifier que le département existe
        if (!departementRepository.existsById(departementId)) {
            throw new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId);
        }

        // Récupérer les médecins du département
        List<Medecin> medecins = medecinRepository.findByDepartementId(departementId);

        // Convertir les entités en DTOs
        return medecins.stream()
                .map(medecinMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InfirmierDTO> getInfirmiersByDepartement(Long departementId) {
        log.info("Récupération des infirmiers du département avec l'ID : {}", departementId);

        // Vérifier que le département existe
        if (!departementRepository.existsById(departementId)) {
            throw new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId);
        }

        // Récupérer les infirmiers du département
        List<Infirmier> infirmiers = infirmierRepository.findByDepartementId(departementId);

        // Convertir les entités en DTOs
        return infirmiers.stream()
                .map(infirmierMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartementStatsDTO getDepartementStats(Long departementId) {
        log.info("Récupération des statistiques du département avec l'ID : {}", departementId);

        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId));

        // Compter le nombre de médecins
        int nombreMedecins = medecinRepository.countByDepartementId(departementId);

        // Compter le nombre d'infirmiers
        int nombreInfirmiers = infirmierRepository.countByDepartementId(departementId);

        // Compter le nombre de patients (via les consultations du département)
        int nombrePatients = patientRepository.countDistinctByConsultationsMedecinDepartementId(departementId);

        // Compter le nombre de consultations
        int nombreConsultations = consultationRepository.countByMedecinDepartementId(departementId);

        // Compter le nombre de rendez-vous
        int nombreRendezVous = rendezVousRepository.countByMedecinDepartementId(departementId);

        // Créer et remplir le DTO de statistiques
        DepartementStatsDTO statsDTO = new DepartementStatsDTO();
        statsDTO.setDepartementId(departement.getId());
        statsDTO.setNomDepartement(departement.getNom());
        statsDTO.setNombreMedecins((long) nombreMedecins);
        statsDTO.setNombreInfirmiers((long) nombreInfirmiers);
        statsDTO.setNombrePatients((long) nombrePatients);
        statsDTO.setNombreConsultations((long) nombreConsultations);
        statsDTO.setNombreRendezVous((long) nombreRendezVous);

        return statsDTO;
    }

    @Override
    public List<DepartementDTO> searchDepartements(String query) {
        log.info("Recherche de départements avec la requête : {}", query);

        String searchTerm = "%" + query.toLowerCase() + "%";

        List<Departement> departements = departementRepository.searchDepartements(searchTerm);

        return departements.stream()
                .map(departementMapper::toDTO)
                .collect(Collectors.toList());
    }



    @Override
    public DepartementDTO assignerChefDepartement(Long departementId, Long personnelId) {
        log.info("Assignation du chef de département avec l'ID personnel : {} au département avec l'ID : {}",
                personnelId, departementId);

        // Récupérer le département
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId));

        // Si personnelId est 0, on retire le chef de département
        if (personnelId == 0) {
            departement.setChefDepartement(null);
            log.info("Chef de département retiré pour le département avec l'ID : {}", departementId);
        } else {
            // Récupérer le personnel
            Personnel personnel = personnelRepository.findById(personnelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Personnel non trouvé avec l'ID : " + personnelId));

            // Vérifier que le personnel appartient au même département
            if (personnel.getDepartement() == null || !personnel.getDepartement().getId().equals(departementId)) {
                throw new IllegalArgumentException("Le personnel doit appartenir au département pour être assigné comme chef");
            }

            // Assigner le chef de département
            departement.setChefDepartement(personnel);
            log.info("Chef de département assigné avec succès");
        }

        // Sauvegarder les modifications
        Departement updatedDepartement = departementRepository.save(departement);

        return departementMapper.toDTO(updatedDepartement);
    }
}
