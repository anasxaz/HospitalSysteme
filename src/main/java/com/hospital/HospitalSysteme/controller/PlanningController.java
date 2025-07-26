package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.StatutPlanning;
import com.hospital.HospitalSysteme.service.PlanningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/plannings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Plannings", description = "API pour la gestion des plannings hospitaliers et leur cycle de vie")
public class PlanningController {

    private final PlanningService planningService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des plannings")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans PlanningController fonctionne");
    }

    // ====================== GESTION CRUD DES PLANNINGS ======================

    @PostMapping
    @Operation(summary = "Créer un nouveau planning",
            description = "Permet de créer un nouveau planning hospitalier avec validation des dates et vérification des chevauchements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Planning créé avec succès",
                    content = @Content(schema = @Schema(implementation = PlanningDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou chevauchement de dates"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<PlanningDTO> createPlanning(
            @Parameter(description = "Informations du planning à créer")
            @Valid @RequestBody PlanningCreationDTO planningCreationDTO) {
        log.info("Demande de création d'un planning du {} au {}",
                planningCreationDTO.getDateDebut(), planningCreationDTO.getDateFin());

        PlanningDTO createdPlanning = planningService.createPlanning(planningCreationDTO);
        return new ResponseEntity<>(createdPlanning, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un planning par ID",
            description = "Récupère les informations détaillées d'un planning par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planning trouvé",
                    content = @Content(schema = @Schema(implementation = PlanningDTO.class))),
            @ApiResponse(responseCode = "404", description = "Planning non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<PlanningDTO> getPlanning(
            @Parameter(description = "ID du planning") @PathVariable Long id) {
        log.info("Demande de récupération du planning avec l'ID: {}", id);

        PlanningDTO planningDTO = planningService.getPlanning(id);
        return ResponseEntity.ok(planningDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les plannings avec pagination",
            description = "Récupère la liste paginée de tous les plannings du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page de plannings récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<Page<PlanningDTO>> getAllPlannings(
            @Parameter(description = "Paramètres de pagination (page, size, sort)")
            @PageableDefault(size = 20, sort = "dateDebut") Pageable pageable) {
        log.info("Demande de récupération de tous les plannings avec pagination");

        Page<PlanningDTO> planningsPage = planningService.getAllPlannings(pageable);
        return ResponseEntity.ok(planningsPage);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un planning",
            description = "Met à jour les informations d'un planning existant avec validation des dates et des chevauchements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planning mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = PlanningDTO.class))),
            @ApiResponse(responseCode = "404", description = "Planning non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou chevauchement de dates")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<PlanningDTO> updatePlanning(
            @Parameter(description = "ID du planning") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour")
            @Valid @RequestBody PlanningUpdateDTO planningUpdateDTO) {
        log.info("Demande de mise à jour du planning avec l'ID: {}", id);

        PlanningDTO updatedPlanning = planningService.updatePlanning(id, planningUpdateDTO);
        return ResponseEntity.ok(updatedPlanning);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un planning",
            description = "Supprime un planning du système (uniquement si pas encore publié)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Planning supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Planning non trouvé"),
            @ApiResponse(responseCode = "400", description = "Impossible de supprimer un planning publié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Void> deletePlanning(
            @Parameter(description = "ID du planning à supprimer") @PathVariable Long id) {
        log.info("Demande de suppression du planning avec l'ID: {}", id);

        planningService.deletePlanning(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== GESTION DES STATUTS ======================

    @PutMapping("/{id}/statut")
    @Operation(summary = "Changer le statut d'un planning",
            description = "Modifie le statut d'un planning avec validation des transitions autorisées")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statut changé avec succès",
                    content = @Content(schema = @Schema(implementation = PlanningDTO.class))),
            @ApiResponse(responseCode = "404", description = "Planning non trouvé"),
            @ApiResponse(responseCode = "400", description = "Transition de statut non autorisée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<PlanningDTO> changerStatut(
            @Parameter(description = "ID du planning") @PathVariable Long id,
            @Parameter(description = "Nouveau statut (BROUILLON, PUBLIE, ARCHIVE)")
            @RequestParam StatutPlanning nouveauStatut) {
        log.info("Demande de changement de statut du planning ID: {} vers: {}", id, nouveauStatut);

        PlanningDTO planningDTO = planningService.changerStatut(id, nouveauStatut);
        return ResponseEntity.ok(planningDTO);
    }

    @PutMapping("/{id}/publier")
    @Operation(summary = "Publier un planning",
            description = "Publie un planning pour le rendre actif et visible")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planning publié avec succès",
                    content = @Content(schema = @Schema(implementation = PlanningDTO.class))),
            @ApiResponse(responseCode = "404", description = "Planning non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<PlanningDTO> publierPlanning(
            @Parameter(description = "ID du planning à publier") @PathVariable Long id) {
        log.info("Demande de publication du planning avec l'ID: {}", id);

        PlanningDTO planningDTO = planningService.publierPlanning(id);
        return ResponseEntity.ok(planningDTO);
    }

    @PutMapping("/{id}/archiver")
    @Operation(summary = "Archiver un planning",
            description = "Archive un planning pour le rendre non modifiable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planning archivé avec succès",
                    content = @Content(schema = @Schema(implementation = PlanningDTO.class))),
            @ApiResponse(responseCode = "404", description = "Planning non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<PlanningDTO> archiverPlanning(
            @Parameter(description = "ID du planning à archiver") @PathVariable Long id) {
        log.info("Demande d'archivage du planning avec l'ID: {}", id);

        PlanningDTO planningDTO = planningService.archiverPlanning(id);
        return ResponseEntity.ok(planningDTO);
    }

    // ====================== FILTRES ET RECHERCHES ======================

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les plannings par statut",
            description = "Récupère tous les plannings ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plannings récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<PlanningDTO>> getPlanningsByStatut(
            @Parameter(description = "Statut des plannings (BROUILLON, PUBLIE, ARCHIVE)")
            @PathVariable StatutPlanning statut) {
        log.info("Demande de récupération des plannings avec le statut: {}", statut);

        List<PlanningDTO> plannings = planningService.getPlanningsByStatut(statut);
        return ResponseEntity.ok(plannings);
    }

    @GetMapping("/departement/{departementId}")
    @Operation(summary = "Récupérer les plannings par département",
            description = "Récupère tous les plannings associés aux cadres administratifs d'un département")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plannings du département récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<PlanningDTO>> getPlanningsByDepartement(
            @Parameter(description = "ID du département") @PathVariable Long departementId) {
        log.info("Demande de récupération des plannings pour le département ID: {}", departementId);

        List<PlanningDTO> plannings = planningService.getPlanningsByDepartement(departementId);
        return ResponseEntity.ok(plannings);
    }

    @GetMapping("/cadre-administratif/{cadreAdministratifId}")
    @Operation(summary = "Récupérer les plannings d'un cadre administratif",
            description = "Récupère tous les plannings associés à un cadre administratif spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plannings du cadre administratif récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Cadre administratif non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<PlanningDTO>> getPlanningsByCadreAdministratif(
            @Parameter(description = "ID du cadre administratif") @PathVariable Long cadreAdministratifId) {
        log.info("Demande de récupération des plannings pour le cadre administratif ID: {}", cadreAdministratifId);

        List<PlanningDTO> plannings = planningService.getPlanningsByCadreAdministratif(cadreAdministratifId);
        return ResponseEntity.ok(plannings);
    }

    @GetMapping("/periode")
    @Operation(summary = "Récupérer les plannings par période",
            description = "Récupère tous les plannings se déroulant dans une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plannings de la période récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<PlanningDTO>> getPlanningsByPeriode(
            @Parameter(description = "Date de début de la période (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @Parameter(description = "Date de fin de la période (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        log.info("Demande de récupération des plannings entre le {} et le {}", debut, fin);

        List<PlanningDTO> plannings = planningService.getPlanningsByPeriode(debut, fin);
        return ResponseEntity.ok(plannings);
    }

    @GetMapping("/cadre-administratif/{cadreAdministratifId}/actifs/date/{date}")
    @Operation(summary = "Récupérer les plannings actifs d'un cadre à une date",
            description = "Récupère tous les plannings actifs (publiés) d'un cadre administratif pour une date spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plannings actifs récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Cadre administratif non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PlanningDTO>> getPlanningsActifsByCadreAdministratifAndDate(
            @Parameter(description = "ID du cadre administratif") @PathVariable Long cadreAdministratifId,
            @Parameter(description = "Date de recherche (format: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Demande de récupération des plannings actifs pour le cadre administratif ID: {} à la date: {}",
                cadreAdministratifId, date);

        List<PlanningDTO> plannings = planningService.getPlanningsActifsByCadreAdministratifAndDate(cadreAdministratifId, date);
        return ResponseEntity.ok(plannings);
    }

    // ====================== UTILITAIRES ======================

    @GetMapping("/check-overlaps")
    @Operation(summary = "Vérifier les chevauchements de plannings",
            description = "Vérifie s'il existe des chevauchements pour une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vérification effectuée avec succès"),
            @ApiResponse(responseCode = "400", description = "Chevauchement détecté")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<String> checkForOverlaps(
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @Parameter(description = "ID du planning à exclure de la vérification (optionnel)")
            @RequestParam(required = false) Long excludePlanningId) {
        log.info("Vérification des chevauchements pour la période du {} au {}", debut, fin);

        try {
            boolean noOverlap = planningService.checkForOverlaps(debut, fin, excludePlanningId);
            if (noOverlap) {
                return ResponseEntity.ok("Aucun chevauchement détecté pour cette période");
            } else {
                return ResponseEntity.badRequest().body("Chevauchement détecté avec d'autres plannings");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}