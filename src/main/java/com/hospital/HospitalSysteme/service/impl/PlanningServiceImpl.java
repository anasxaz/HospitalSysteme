package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.PlanningCreationDTO;
import com.hospital.HospitalSysteme.dto.PlanningDTO;
import com.hospital.HospitalSysteme.dto.PlanningUpdateDTO;
import com.hospital.HospitalSysteme.entity.CadreAdministratif;
import com.hospital.HospitalSysteme.entity.Commande;
import com.hospital.HospitalSysteme.entity.PlanDeSoins;
import com.hospital.HospitalSysteme.entity.Planning;
import com.hospital.HospitalSysteme.entity.enums.StatutCommande;
import com.hospital.HospitalSysteme.entity.enums.StatutPlanning;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.CommandeMapper;
import com.hospital.HospitalSysteme.mapper.PlanDeSoinsMapper;
import com.hospital.HospitalSysteme.mapper.PlanningMapper;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.PlanningService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class PlanningServiceImpl implements PlanningService {

    // Dependency Injection
    private PatientRepository patientRepository;
    private PlanDeSoinsRepository planDeSoinsRepository;
    private InfirmierRepository infirmierRepository;
    private CommandeRepository commandeRepository;
    private CadreAdministratifRepository cadreAdministratifRepository;
    private PlanningRepository planningRepository;


    private PlanDeSoinsMapper planDeSoinsMapper;
    private CommandeMapper commandeMapper;
    private PlanningMapper planningMapper;






//    @Override
//    public PlanningDTO createPlanning(PlanningCreationDTO planningCreationDTO) {
//        log.info("Création d'un nouveau planning le : {}", planningCreationDTO.getDateDebut());
//
//        // Vérifier si la date de début est avant la date de fin ou pas
//        if (planningCreationDTO.getDateFin().isBefore(planningCreationDTO.getDateDebut())) {
//            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
//        }
//
//        // Convert planningCreationDTO to planning JPA Entity
//        Planning planning = planningMapper.toEntity(planningCreationDTO);
//
//        // Définir le statut par défaut si non spécifié
//        if (planning.getStatut() == null) {
//            planning.setStatut(StatutPlanning.BROUILLON);
//        }
//
//        // planning JPA Entity
//        Planning savedPlanning = planningRepository.save(planning);
//
//        log.info("Planning crée avec succès avec l'ID : {}", savedPlanning.getId());
//
//        // Convert savedPlanning JPA Entity into DTO object
//        return planningMapper.toDTO(savedPlanning);
//    }

    //2ème tentative :
//    @Override
//    public PlanningDTO createPlanning(PlanningCreationDTO planningCreationDTO) {
//        log.info("Création d'un nouveau planning le : {}", planningCreationDTO.getDateDebut());
//
//        // Validation des dates
//        if (planningCreationDTO.getDateFin().isBefore(planningCreationDTO.getDateDebut())) {
//            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
//        }
//
//        Planning planning = planningMapper.toEntity(planningCreationDTO);
//
//        // AJOUTEZ : Récupérer et assigner les cadres administratifs
//        Set<CadreAdministratif> cadres = new HashSet<>();
//        for (Long cadreId : planningCreationDTO.getCadreAdministratifIds()) {
//            CadreAdministratif cadre = cadreAdministratifRepository.findById(cadreId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreId));
//            cadres.add(cadre);
//        }
//        planning.setCadreAdministratifs(cadres);
//
//        // Statut par défaut
//        if (planning.getStatut() == null) {
//            planning.setStatut(StatutPlanning.BROUILLON);
//        }
//
//        Planning savedPlanning = planningRepository.save(planning);
//        log.info("Planning créé avec succès avec l'ID : {}", savedPlanning.getId());
//
//        return planningMapper.toDTO(savedPlanning);
//    }

    @Override
    public PlanningDTO createPlanning(PlanningCreationDTO planningCreationDTO) {
        log.info("Création d'un nouveau planning le : {}", planningCreationDTO.getDateDebut());

        // Vérifier si la date de début est avant la date de fin ou pas
        if (planningCreationDTO.getDateFin().isBefore(planningCreationDTO.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        // Convert planningCreationDTO to planning JPA Entity
        Planning planning = planningMapper.toEntity(planningCreationDTO);

        // AJOUT : Récupérer et assigner les cadres administratifs
        Set<CadreAdministratif> cadres = new HashSet<>();

        if (planningCreationDTO.getCadreAdministratifIds() != null && !planningCreationDTO.getCadreAdministratifIds().isEmpty()) {
            for (Long cadreId : planningCreationDTO.getCadreAdministratifIds()) {
                CadreAdministratif cadre = cadreAdministratifRepository.findById(cadreId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreId));
                cadres.add(cadre);
                log.info("Cadre administratif ajouté au planning : {}", cadre.getNom());
            }
            planning.setCadreAdministratifs(cadres);
        } else {
            throw new IllegalArgumentException("Au moins un cadre administratif doit être assigné au planning");
        }

        // Définir le statut par défaut si non spécifié
        if (planning.getStatut() == null) {
            planning.setStatut(StatutPlanning.BROUILLON);
        }

        // Vérifier les chevauchements AVANT de sauvegarder
        checkForOverlaps(planning.getDateDebut(), planning.getDateFin(), null);

        // Sauvegarder le planning JPA Entity
        Planning savedPlanning = planningRepository.save(planning);

        log.info("Planning créé avec succès avec l'ID : {}", savedPlanning.getId());

        // Convert savedPlanning JPA Entity into DTO object
        return planningMapper.toDTO(savedPlanning);
    }

    @Override
    public PlanningDTO getPlanning(Long id) {
        log.info("Récupération du planning avec l'ID : {}", id);

        Planning planning = planningRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Planning non trouvé avec l'ID : " + id)
        );

        return planningMapper.toDTO(planning);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public Page<PlanningDTO> getAllPlannings(Pageable pageable) {
        log.info("Récupération de tous les plannings avec pagination");
        Page<Planning> planningPage = planningRepository.findAll(pageable);
        return planningPage.map(planningMapper::toDTO);
    }

    @Override
    public List<PlanningDTO> getPlanningsByStatut(StatutPlanning statut) {
        log.info("Récupération des planning avec le statut : {}", statut);

        List<Planning> plannings = planningRepository.findByStatut(statut);

        return plannings.stream()
                .map(planningMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je pense que cette méthode ne doit pas exister s'il n'y pas de lien directe entre les deux
    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PlanningDTO> getPlanningsByDepartement(Long departementId) {
        log.info("Récupération des plannings pour le département avec l'ID : {}", departementId);

        // Vérifier que le département existe
        // Note: Vous devriez avoir un DepartementRepository pour cette vérification
        // Si vous n'en avez pas, vous pouvez omettre cette vérification

        // Récupérer les cadres administratifs du département
        List<CadreAdministratif> cadresAdministratifs = cadreAdministratifRepository.findByDepartementId(departementId);

        if (cadresAdministratifs.isEmpty()) {
            log.warn("Aucun cadre administratif trouvé pour le département avec l'ID : {}", departementId);
            return List.of();
        }

        // Récupérer les plannings associés à ces cadres administratifs
        List<Planning> plannings = planningRepository.findByCadreAdministratifsIn(cadresAdministratifs);

        return plannings.stream()
                .distinct() // Pour éviter les doublons
                .map(planningMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PlanningDTO> getPlanningsByCadreAdministratif(Long cadreAdministratifId) {
        log.info("Récupération des plannings pour le cadre administratif avec l'ID : {}", cadreAdministratifId);

        // Vérifier que le cadre administratif existe
        CadreAdministratif cadreAdministratif = cadreAdministratifRepository.findById(cadreAdministratifId)
                .orElseThrow(() -> new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreAdministratifId));

        List<Planning> plannings = planningRepository.findByCadreAdministratifsContains(cadreAdministratif);

        return plannings.stream()
                .map(planningMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanningDTO> getPlanningsByPeriode(LocalDate debut, LocalDate fin) {
        log.info("Récupération des plannings entre le : {} et le : {}", debut,fin);

        List<Planning> plannings = planningRepository.findByDateDebutGreaterThanEqualAndDateFinLessThanEqual(debut, fin);

        return plannings.stream()
                .map(planningMapper::toDTO)
                .collect(Collectors.toList());
    }

//    @Override
//    public PlanningDTO updatePlanning(Long id, PlanningUpdateDTO planningUpdateDTO) {
//        log.info("Mise à jour du planning avec l'ID : {}", id);
//
//        // Récupérer le planning existant
//        Planning planning = planningRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Planning non trouvé avec l'ID : " + id));
//
//        // Vérifier si les dates sont valides si elles sont modifiées
//        if (planningUpdateDTO.getDateDebut() != null && planningUpdateDTO.getDateFin() != null) {
//            if (planningUpdateDTO.getDateFin().isBefore(planningUpdateDTO.getDateDebut())) {
//                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
//            }
//
//            // Vérifier les chevauchements
//            checkForOverlaps(planningUpdateDTO.getDateDebut(), planningUpdateDTO.getDateFin(), id);
//        }
//
//        // Mettre à jour les champs du planning
//        planningMapper.updateEntityFromDTO(planningUpdateDTO, planning);
//
//        // Sauvegarder les modifications
//        Planning updatedPlanning = planningRepository.save(planning);
//        log.info("Planning mis à jour avec succès");
//
//        return planningMapper.toDTO(updatedPlanning);
//    }


    @Override
    public PlanningDTO updatePlanning(Long id, PlanningUpdateDTO planningUpdateDTO) {
        log.info("Mise à jour du planning avec l'ID : {}", id);

        // Récupérer le planning existant
        Planning planning = planningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Planning non trouvé avec l'ID : " + id));

        // Vérifier si les dates sont valides si elles sont modifiées
        if (planningUpdateDTO.getDateDebut() != null && planningUpdateDTO.getDateFin() != null) {
            if (planningUpdateDTO.getDateFin().isBefore(planningUpdateDTO.getDateDebut())) {
                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
            }

            // Vérifier les chevauchements
            checkForOverlaps(planningUpdateDTO.getDateDebut(), planningUpdateDTO.getDateFin(), id);
        }

        // AJOUTEZ CETTE PARTIE - Mettre à jour les cadres administratifs
        if (planningUpdateDTO.getCadreAdministratifIds() != null) {
            Set<CadreAdministratif> nouveauxCadres = new HashSet<>();
            for (Long cadreId : planningUpdateDTO.getCadreAdministratifIds()) {
                CadreAdministratif cadre = cadreAdministratifRepository.findById(cadreId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreId));
                nouveauxCadres.add(cadre);
            }
            planning.setCadreAdministratifs(nouveauxCadres);
        }

        // Mettre à jour les autres champs du planning
        planningMapper.updateEntityFromDTO(planningUpdateDTO, planning);

        // Sauvegarder les modifications
        Planning updatedPlanning = planningRepository.save(planning);
        log.info("Planning mis à jour avec succès");

        return planningMapper.toDTO(updatedPlanning);
    }



    @Override
    public void deletePlanning(Long id) {
        log.info("Suppression du planning avec l'ID : {}", id);

        // Récupérer le planning
        Planning planning = planningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Planning non trouvé avec l'ID : " + id));

        // Vérifier que le planning peut être supprimé (pas PUBLIE)
        if (planning.getStatut() == StatutPlanning.PUBLIE) {
            throw new IllegalStateException("Impossible de supprimer un planning publié");
        }

        // Supprimer le planning
        planningRepository.delete(planning);
        log.info("Planning supprimé avec succès");
    }

    @Override
    public PlanningDTO changerStatut(Long id, StatutPlanning nouveauStatut) {
        log.info("Changement du statut du planning {} vers {}", id, nouveauStatut);

        // Récupérer le planning
        Planning planning = planningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Planning non trouvé avec l'ID : " + id));

        // Validation des transitions de statut
        validateStatusTransition(planning.getStatut(), nouveauStatut);

        // Changer le statut
        planning.setStatut(nouveauStatut);
        Planning updatedPlanning = planningRepository.save(planning);

        log.info("Statut du planning mis à jour avec succès");
        return planningMapper.toDTO(updatedPlanning);
    }

    // Méthode utilitaire pour valider les transitions de statut
    private void validateStatusTransition(StatutPlanning statutActuel, StatutPlanning nouveauStatut) {
        // Règles de transition de statut
        if (statutActuel == StatutPlanning.ARCHIVE && nouveauStatut != StatutPlanning.ARCHIVE) {
            throw new IllegalStateException("Un planning archivé ne peut pas changer de statut");
        }

        if (statutActuel == StatutPlanning.PUBLIE && nouveauStatut == StatutPlanning.BROUILLON) {
            throw new IllegalStateException("Un planning publié ne peut pas revenir au statut brouillon");
        }
    }

    @Override
    public boolean checkForOverlaps(LocalDate debut, LocalDate fin, Long excludePlanningId) {
        log.debug("Vérification des chevauchements pour la période du {} au {}", debut, fin);
        List<Planning> overlappingPlannings;

        if (excludePlanningId != null) {
            overlappingPlannings = planningRepository.findOverlappingPlanningsExcluding(debut, fin, excludePlanningId);
        } else {
            overlappingPlannings = planningRepository.findOverlappingPlannings(debut, fin);
        }

        if (!overlappingPlannings.isEmpty()) {
            log.warn("Chevauchement détecté avec {} plannings existants", overlappingPlannings.size());
            throw new IllegalStateException("La période du planning chevauche des plannings existants");
        }

        return overlappingPlannings.isEmpty();
    }

    @Override
    public PlanningDTO publierPlanning(Long id) {
        log.info("Publication du planning avec l'ID : {}", id);
        return changerStatut(id, StatutPlanning.PUBLIE);
    }

    @Override
    public PlanningDTO archiverPlanning(Long id) {
        log.info("Archivage du planning avec l'ID : {}", id);
        return changerStatut(id, StatutPlanning.ARCHIVE);
    }

    @Override
    public List<PlanningDTO> getPlanningsActifsByCadreAdministratifAndDate(Long cadreAdministratifId, LocalDate date) {
        log.info("Récupération des plannings actifs pour le cadre administratif {} à la date {}", cadreAdministratifId, date);

        // Vérifier que le cadre administratif existe
        CadreAdministratif cadreAdministratif = cadreAdministratifRepository.findById(cadreAdministratifId)
                .orElseThrow(() -> new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreAdministratifId));

        // Récupérer les plannings actifs (PUBLIE) pour ce cadre administratif à cette date
        List<Planning> plannings = planningRepository.findByCadreAdministratifsContainsAndStatutAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                cadreAdministratif, StatutPlanning.PUBLIE, date, date);

        return plannings.stream()
                .map(planningMapper::toDTO)
                .collect(Collectors.toList());
    }
}
