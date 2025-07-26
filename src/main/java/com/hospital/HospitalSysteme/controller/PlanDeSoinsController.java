package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.StatutPlanDeSoins;
import com.hospital.HospitalSysteme.service.PlanDeSoinsService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/plans-de-soins")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Plans de Soins", description = "API pour la gestion des plans de soins infirmiers et leur suivi")
public class PlanDeSoinsController {

    private final PlanDeSoinsService planDeSoinsService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des plans de soins")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans PlanDeSoinsController fonctionne");
    }

    // ====================== GESTION CRUD DES PLANS DE SOINS ======================

    @PostMapping
    @Operation(summary = "Créer un nouveau plan de soins",
            description = "Permet de créer un nouveau plan de soins pour un patient avec un infirmier assigné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan de soins créé avec succès",
                    content = @Content(schema = @Schema(implementation = PlanDeSoinsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou dates incohérentes"),
            @ApiResponse(responseCode = "404", description = "Patient ou infirmier non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<PlanDeSoinsDTO> createPlanDeSoins(
            @Parameter(description = "Informations du plan de soins à créer")
            @Valid @RequestBody PlanDeSoinsCreationDTO planDeSoinsCreationDTO) {
        log.info("Demande de création d'un plan de soins du {} au {} pour le patient ID: {}",
                planDeSoinsCreationDTO.getDateDebut(), planDeSoinsCreationDTO.getDateFin(),
                planDeSoinsCreationDTO.getPatientId());

        PlanDeSoinsDTO createdPlanDeSoins = planDeSoinsService.createPlanDeSoins(planDeSoinsCreationDTO);
        return new ResponseEntity<>(createdPlanDeSoins, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un plan de soins par ID",
            description = "Récupère les informations détaillées d'un plan de soins par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan de soins trouvé",
                    content = @Content(schema = @Schema(implementation = PlanDeSoinsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Plan de soins non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<PlanDeSoinsDTO> getPlanDeSoinsById(
            @Parameter(description = "ID du plan de soins") @PathVariable Long id) {
        log.info("Demande de récupération du plan de soins avec l'ID: {}", id);

        PlanDeSoinsDTO planDeSoinsDTO = planDeSoinsService.getPlanDeSoinsById(id);
        return ResponseEntity.ok(planDeSoinsDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les plans de soins",
            description = "Récupère la liste complète de tous les plans de soins du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des plans de soins récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getAllPlansDeSoins() {
        log.info("Demande de récupération de tous les plans de soins");

        List<PlanDeSoinsDTO> plansDeSoinsList = planDeSoinsService.getAllPlansDeSoins();
        return ResponseEntity.ok(plansDeSoinsList);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un plan de soins",
            description = "Met à jour les informations d'un plan de soins existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan de soins mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = PlanDeSoinsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Plan de soins non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<PlanDeSoinsDTO> updatePlanDeSoins(
            @Parameter(description = "ID du plan de soins") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour")
            @Valid @RequestBody PlanDeSoinsUpdateDTO planDeSoinsUpdateDTO) {
        log.info("Demande de mise à jour du plan de soins avec l'ID: {}", id);

        PlanDeSoinsDTO updatedPlanDeSoins = planDeSoinsService.updatePlanDeSoins(id, planDeSoinsUpdateDTO);
        return ResponseEntity.ok(updatedPlanDeSoins);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un plan de soins",
            description = "Supprime définitivement un plan de soins du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plan de soins supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Plan de soins non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
    public ResponseEntity<Void> deletePlanDeSoins(
            @Parameter(description = "ID du plan de soins à supprimer") @PathVariable Long id) {
        log.info("Demande de suppression du plan de soins avec l'ID: {}", id);

        planDeSoinsService.deletePlanDeSoins(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== FILTRES ET RECHERCHES ======================

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Récupérer les plans de soins d'un patient",
            description = "Récupère tous les plans de soins associés à un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans de soins du patient récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getPlansDeSoinsByPatientId(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de récupération des plans de soins pour le patient ID: {}", patientId);

        List<PlanDeSoinsDTO> plansDeSoins = planDeSoinsService.getPlansDeSoinsByPatientId(patientId);
        return ResponseEntity.ok(plansDeSoins);
    }

    @GetMapping("/infirmier/{infirmierId}")
    @Operation(summary = "Récupérer les plans de soins d'un infirmier",
            description = "Récupère tous les plans de soins assignés à un infirmier spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans de soins de l'infirmier récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Infirmier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getPlansDeSoinsByInfirmierId(
            @Parameter(description = "ID de l'infirmier") @PathVariable Long infirmierId) {
        log.info("Demande de récupération des plans de soins pour l'infirmier ID: {}", infirmierId);

        List<PlanDeSoinsDTO> plansDeSoins = planDeSoinsService.getPlansDeSoinsByInfirmierId(infirmierId);
        return ResponseEntity.ok(plansDeSoins);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les plans de soins par statut",
            description = "Récupère tous les plans de soins ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans de soins par statut récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getPlansDeSoinsByStatut(
            @Parameter(description = "Statut des plans de soins (EN_COURS, TERMINE, SUSPENDU, ANNULE)")
            @PathVariable StatutPlanDeSoins statut) {
        log.info("Demande de récupération des plans de soins avec le statut: {}", statut);

        List<PlanDeSoinsDTO> plansDeSoins = planDeSoinsService.getPlansDeSoinsByStatut(statut);
        return ResponseEntity.ok(plansDeSoins);
    }

    @GetMapping("/actifs")
    @Operation(summary = "Récupérer les plans de soins actifs",
            description = "Récupère tous les plans de soins actuellement en cours (statut EN_COURS)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans de soins actifs récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getPlansDeSoinsActifs() {
        log.info("Demande de récupération des plans de soins actifs");

        List<PlanDeSoinsDTO> plansDeSoins = planDeSoinsService.getPlansDeSoinsActifs();
        return ResponseEntity.ok(plansDeSoins);
    }

    @GetMapping("/periode")
    @Operation(summary = "Récupérer les plans de soins par période",
            description = "Récupère tous les plans de soins se déroulant dans une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans de soins de la période récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getPlansDeSoinsEntreDates(
            @Parameter(description = "Date de début de la période (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin de la période (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        log.info("Demande de récupération des plans de soins entre le {} et le {}", dateDebut, dateFin);

        List<PlanDeSoinsDTO> plansDeSoins = planDeSoinsService.getPlansDeSoinsEntreDates(dateDebut, dateFin);
        return ResponseEntity.ok(plansDeSoins);
    }

    // ====================== GESTION DES STATUTS ======================

    @PutMapping("/{id}/statut")
    @Operation(summary = "Changer le statut d'un plan de soins",
            description = "Modifie le statut d'un plan de soins existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statut changé avec succès",
                    content = @Content(schema = @Schema(implementation = PlanDeSoinsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Plan de soins non trouvé"),
            @ApiResponse(responseCode = "400", description = "Statut invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<PlanDeSoinsDTO> changerStatutPlanDeSoins(
            @Parameter(description = "ID du plan de soins") @PathVariable Long id,
            @Parameter(description = "Nouveau statut (EN_COURS, TERMINE, SUSPENDU, ANNULE)")
            @RequestParam StatutPlanDeSoins nouveauStatut) {
        log.info("Demande de changement de statut du plan de soins ID: {} vers: {}", id, nouveauStatut);

        PlanDeSoinsDTO planDeSoinsDTO = planDeSoinsService.changerStatutPlanDeSoins(id, nouveauStatut);
        return ResponseEntity.ok(planDeSoinsDTO);
    }

    @PutMapping("/{id}/terminer")
    @Operation(summary = "Terminer un plan de soins",
            description = "Marque un plan de soins comme terminé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan de soins terminé avec succès",
                    content = @Content(schema = @Schema(implementation = PlanDeSoinsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Plan de soins non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<PlanDeSoinsDTO> terminerPlanDeSoins(
            @Parameter(description = "ID du plan de soins à terminer") @PathVariable Long id) {
        log.info("Demande de terminaison du plan de soins avec l'ID: {}", id);

        PlanDeSoinsDTO planDeSoinsDTO = planDeSoinsService.changerStatutPlanDeSoins(id, StatutPlanDeSoins.TERMINE);
        return ResponseEntity.ok(planDeSoinsDTO);
    }

    @PutMapping("/{id}/suspendre")
    @Operation(summary = "Suspendre un plan de soins",
            description = "Marque un plan de soins comme suspendu temporairement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan de soins suspendu avec succès",
                    content = @Content(schema = @Schema(implementation = PlanDeSoinsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Plan de soins non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<PlanDeSoinsDTO> suspendrePlanDeSoins(
            @Parameter(description = "ID du plan de soins à suspendre") @PathVariable Long id) {
        log.info("Demande de suspension du plan de soins avec l'ID: {}", id);

        PlanDeSoinsDTO planDeSoinsDTO = planDeSoinsService.changerStatutPlanDeSoins(id, StatutPlanDeSoins.ANNULE);   // C'est ANNULE au lieu de SUSPENDU
        return ResponseEntity.ok(planDeSoinsDTO);
    }

    @PutMapping("/{id}/reprendre")
    @Operation(summary = "Reprendre un plan de soins suspendu",
            description = "Remet en cours un plan de soins précédemment suspendu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan de soins repris avec succès",
                    content = @Content(schema = @Schema(implementation = PlanDeSoinsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Plan de soins non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<PlanDeSoinsDTO> reprendrePlanDeSoins(
            @Parameter(description = "ID du plan de soins à reprendre") @PathVariable Long id) {
        log.info("Demande de reprise du plan de soins avec l'ID: {}", id);

        PlanDeSoinsDTO planDeSoinsDTO = planDeSoinsService.changerStatutPlanDeSoins(id, StatutPlanDeSoins.EN_COURS);
        return ResponseEntity.ok(planDeSoinsDTO);
    }

    // ====================== STATISTIQUES ET COMPTEURS ======================

    @GetMapping("/stats/count-by-statut/{statut}")
    @Operation(summary = "Compter les plans de soins par statut",
            description = "Compte le nombre de plans de soins ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de plans de soins récupéré avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countPlansDeSoinsByStatut(
            @Parameter(description = "Statut à analyser") @PathVariable StatutPlanDeSoins statut) {
        log.info("Demande de comptage des plans de soins pour le statut: {}", statut);

        int count = planDeSoinsService.countPlansDeSoinsByStatut(statut);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/count-by-patient/{patientId}")
    @Operation(summary = "Compter les plans de soins d'un patient",
            description = "Compte le nombre total de plans de soins pour un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de plans de soins récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countPlansDeSoinsByPatientId(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de comptage des plans de soins pour le patient ID: {}", patientId);

        int count = planDeSoinsService.countPlansDeSoinsByPatientId(patientId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/count-by-infirmier/{infirmierId}")
    @Operation(summary = "Compter les plans de soins d'un infirmier",
            description = "Compte le nombre total de plans de soins assignés à un infirmier spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de plans de soins récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Infirmier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countPlansDeSoinsByInfirmierId(
            @Parameter(description = "ID de l'infirmier") @PathVariable Long infirmierId) {
        log.info("Demande de comptage des plans de soins pour l'infirmier ID: {}", infirmierId);

        int count = planDeSoinsService.countPlansDeSoinsByInfirmierId(infirmierId);
        return ResponseEntity.ok(count);
    }
}