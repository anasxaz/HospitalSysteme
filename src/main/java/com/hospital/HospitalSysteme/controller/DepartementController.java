package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.DepartementService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departements")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Départements", description = "API pour la gestion des départements")
public class DepartementController {

    private final DepartementService departementService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans DepartementController fonctionne");
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau département", description = "Permet de créer un nouveau département dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Département créé",
                    content = @Content(schema = @Schema(implementation = DepartementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou nom déjà utilisé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartementDTO> createDepartement(
            @Parameter(description = "Informations du département à créer") @Valid @RequestBody DepartementCreationDTO departementCreationDTO) {
        log.info("Demande de création d'un nouveau département avec le nom : {}", departementCreationDTO.getNom());
        DepartementDTO createdDepartement = departementService.createDepartement(departementCreationDTO);
        return new ResponseEntity<>(createdDepartement, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un département par ID", description = "Récupère les informations d'un département par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Département trouvé"),
            @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<DepartementDTO> getDepartementById(
            @Parameter(description = "ID du département") @PathVariable Long id) {
        log.info("Récupération du département avec l'ID : {}", id);
        DepartementDTO departement = departementService.getDepartementById(id);
        return ResponseEntity.ok(departement);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les départements", description = "Récupère la liste de tous les départements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des départements récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<List<DepartementDTO>> getAllDepartements() {
        log.info("Récupération de tous les départements");
        List<DepartementDTO> departements = departementService.getAllDepartements();
        return ResponseEntity.ok(departements);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un département", description = "Met à jour les informations d'un département existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Département mis à jour"),
            @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartementDTO> updateDepartement(
            @Parameter(description = "ID du département") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour") @Valid @RequestBody DepartementUpdateDTO departementUpdateDTO) {
        log.info("Mise à jour du département avec l'ID : {}", id);
        DepartementDTO updatedDepartement = departementService.updateDepartement(id, departementUpdateDTO);
        return ResponseEntity.ok(updatedDepartement);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un département", description = "Supprime un département du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Département supprimé"),
            @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartement(
            @Parameter(description = "ID du département") @PathVariable Long id) {
        log.info("Suppression du département avec l'ID : {}", id);
        departementService.deleteDepartement(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{departementId}/medecins/{medecinId}")
    @Operation(summary = "Assigner un médecin à un département",
            description = "Assigne un médecin existant à un département spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médecin assigné avec succès"),
            @ApiResponse(responseCode = "404", description = "Médecin ou département non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignerMedecinADepartement(
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId,
            @Parameter(description = "ID du département") @PathVariable Long departementId) {
        log.info("Assignation du médecin avec l'ID : {} au département avec l'ID : {}", medecinId, departementId);
        departementService.assignerMedecinADepartement(medecinId, departementId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{departementId}/infirmiers/{infirmierId}")
    @Operation(summary = "Assigner un infirmier à un département",
            description = "Assigne un infirmier existant à un département spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Infirmier assigné avec succès"),
            @ApiResponse(responseCode = "404", description = "Infirmier ou département non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignerInfirmierADepartement(
            @Parameter(description = "ID de l'infirmier") @PathVariable Long infirmierId,
            @Parameter(description = "ID du département") @PathVariable Long departementId) {
        log.info("Assignation de l'infirmier avec l'ID : {} au département avec l'ID : {}", infirmierId, departementId);
        departementService.assignerInfirmierADepartement(infirmierId, departementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/medecins")
    @Operation(summary = "Récupérer les médecins d'un département",
            description = "Récupère la liste des médecins affectés à un département spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médecins récupérée"),
            @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<List<MedecinDTO>> getMedecinsByDepartement(
            @Parameter(description = "ID du département") @PathVariable Long id) {
        log.info("Récupération des médecins du département avec l'ID : {}", id);
        List<MedecinDTO> medecins = departementService.getMedecinsByDepartement(id);
        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/{id}/infirmiers")
    @Operation(summary = "Récupérer les infirmiers d'un département",
            description = "Récupère la liste des infirmiers affectés à un département spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des infirmiers récupérée"),
            @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<List<InfirmierDTO>> getInfirmiersByDepartement(
            @Parameter(description = "ID du département") @PathVariable Long id) {
        log.info("Récupération des infirmiers du département avec l'ID : {}", id);
        List<InfirmierDTO> infirmiers = departementService.getInfirmiersByDepartement(id);
        return ResponseEntity.ok(infirmiers);
    }

    @GetMapping("/{id}/stats")
    @Operation(summary = "Récupérer les statistiques d'un département",
            description = "Récupère les statistiques détaillées d'un département spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées"),
            @ApiResponse(responseCode = "404", description = "Département non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<DepartementStatsDTO> getDepartementStats(
            @Parameter(description = "ID du département") @PathVariable Long id) {
        log.info("Récupération des statistiques du département avec l'ID : {}", id);
        DepartementStatsDTO stats = departementService.getDepartementStats(id);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des départements",
            description = "Recherche des départements par nom ou description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des départements correspondants récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<List<DepartementDTO>> searchDepartements(
            @Parameter(description = "Terme de recherche") @RequestParam String query) {
        log.info("Recherche de départements avec le terme : {}", query);
        List<DepartementDTO> departements = departementService.searchDepartements(query);
        return ResponseEntity.ok(departements);
    }


    @PutMapping("/{departementId}/chef/{personnelId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assigner un chef de département",
            description = "Assigne un membre du personnel comme chef d'un département spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chef de département assigné avec succès"),
            @ApiResponse(responseCode = "404", description = "Département ou personnel non trouvé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    public ResponseEntity<DepartementDTO> assignerChefDepartement(
            @PathVariable Long departementId,
            @PathVariable Long personnelId) {

        DepartementDTO departementDTO = departementService.assignerChefDepartement(departementId, personnelId);
        return ResponseEntity.ok(departementDTO);
    }
}