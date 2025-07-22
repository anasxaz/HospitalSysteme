package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.PlanningCreationDTO;
import com.hospital.HospitalSysteme.dto.PlanningDTO;
import com.hospital.HospitalSysteme.dto.PlanningUpdateDTO;
import com.hospital.HospitalSysteme.entity.enums.StatutPlanning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PlanningService {

    /**
     * Crée un nouveau planning
     * @param planningCreationDTO DTO contenant les informations pour la création du planning
     * @return Le planning créé
     */
    PlanningDTO createPlanning(PlanningCreationDTO planningCreationDTO);

    /**
     * Récupère un planning par son ID
     * @param id ID du planning à récupérer
     * @return Le planning correspondant à l'ID
     */
    PlanningDTO getPlanning(Long id);

    /**
     * Récupère tous les plannings avec pagination
     * @param pageable Informations de pagination
     * @return Une page de plannings
     */
    Page<PlanningDTO> getAllPlannings(Pageable pageable);

    /**
     * Récupère les plannings par statut
     * @param statut Statut des plannings à récupérer
     * @return Liste des plannings correspondant au statut
     */
    List<PlanningDTO> getPlanningsByStatut(StatutPlanning statut);

    /**
     * Récupère les plannings associés aux cadres administratifs d'un département
     * @param departementId ID du département
     * @return Liste des plannings associés au département
     */
    List<PlanningDTO> getPlanningsByDepartement(Long departementId);

    /**
     * Récupère les plannings associés à un cadre administratif
     * @param cadreAdministratifId ID du cadre administratif
     * @return Liste des plannings associés au cadre administratif
     */
    List<PlanningDTO> getPlanningsByCadreAdministratif(Long cadreAdministratifId);

    /**
     * Récupère les plannings pour une période donnée
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des plannings pour la période
     */
    List<PlanningDTO> getPlanningsByPeriode(LocalDate debut, LocalDate fin);

    /**
     * Met à jour un planning existant
     * @param id ID du planning à mettre à jour
     * @param planningUpdateDTO DTO contenant les informations de mise à jour
     * @return Le planning mis à jour
     */
    PlanningDTO updatePlanning(Long id, PlanningUpdateDTO planningUpdateDTO);

    /**
     * Supprime un planning
     * @param id ID du planning à supprimer
     */
    void deletePlanning(Long id);

    /**
     * Change le statut d'un planning
     * @param id ID du planning
     * @param nouveauStatut Nouveau statut du planning
     * @return Le planning avec le statut mis à jour
     */
    PlanningDTO changerStatut(Long id, StatutPlanning nouveauStatut);

    /**
     * Vérifie s'il y a des chevauchements de plannings pour une période donnée
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @param excludePlanningId ID du planning à exclure de la vérification (utile pour les mises à jour)
     * @return true s'il n'y a pas de chevauchement, false sinon
     */
    boolean checkForOverlaps(LocalDate debut, LocalDate fin, Long excludePlanningId);

    /**
     * Publie un planning (change son statut à PUBLIE)
     * @param id ID du planning à publier
     * @return Le planning publié
     */
    PlanningDTO publierPlanning(Long id);

    /**
     * Archive un planning (change son statut à ARCHIVE)
     * @param id ID du planning à archiver
     * @return Le planning archivé
     */
    PlanningDTO archiverPlanning(Long id);

    /**
     * Récupère les plannings actifs (PUBLIE) pour un cadre administratif à une date donnée
     * @param cadreAdministratifId ID du cadre administratif
     * @param date Date pour laquelle vérifier les plannings
     * @return Liste des plannings actifs pour le cadre administratif à la date donnée
     */
    List<PlanningDTO> getPlanningsActifsByCadreAdministratifAndDate(Long cadreAdministratifId, LocalDate date);
}