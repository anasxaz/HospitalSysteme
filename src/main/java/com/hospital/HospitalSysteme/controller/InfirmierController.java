package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.InfirmierService;
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
@RequestMapping("/api/infirmiers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Infirmiers", description = "API pour la gestion des infirmiers et de leurs activités")
public class InfirmierController {

    private final InfirmierService infirmierService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des infirmiers")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans InfirmierController fonctionne");
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau infirmier", description = "Permet de créer un nouvel infirmier dans le système hospitalier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Infirmier créé avec succès",
                    content = @Content(schema = @Schema(implementation = InfirmierDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou email déjà existant"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<InfirmierDTO> createInfirmier(
            @Parameter(description = "Informations de l'infirmier à créer")
            @Valid @RequestBody InfirmierCreationDTO infirmierCreationDTO) {
        log.info("Demande de création d'un infirmier avec l'email: {}", infirmierCreationDTO.getEmail());

        InfirmierDTO createdInfirmier = infirmierService.createInfirmier(infirmierCreationDTO);
        return new ResponseEntity<>(createdInfirmier, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un infirmier par ID", description = "Récupère les informations détaillées d'un infirmier par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Infirmier trouvé",
                    content = @Content(schema = @Schema(implementation = InfirmierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Infirmier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<InfirmierDTO> getInfirmierById(
            @Parameter(description = "ID de l'infirmier") @PathVariable Long id) {
        log.info("Demande de récupération de l'infirmier avec l'ID: {}", id);

        InfirmierDTO infirmierDTO = infirmierService.getInfirmierById(id);
        return ResponseEntity.ok(infirmierDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les infirmiers", description = "Récupère la liste complète de tous les infirmiers du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des infirmiers récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<InfirmierDTO>> getAllInfirmiers() {
        log.info("Demande de récupération de tous les infirmiers");

        List<InfirmierDTO> infirmierList = infirmierService.getAllInfirmiers();
        return ResponseEntity.ok(infirmierList);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un infirmier", description = "Supprime définitivement un infirmier du système hospitalier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Infirmier supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Infirmier non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInfirmier(
            @Parameter(description = "ID de l'infirmier à supprimer") @PathVariable Long id) {
        log.info("Demande de suppression de l'infirmier avec l'ID: {}", id);

        infirmierService.deleteInfirmier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/plans-de-soins")
    @Operation(summary = "Récupérer les plans de soins d'un infirmier",
            description = "Récupère tous les plans de soins assignés à un infirmier spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans de soins récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Infirmier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getInfirmierPlansDeSoins(
            @Parameter(description = "ID de l'infirmier") @PathVariable Long id) {
        log.info("Demande de récupération des plans de soins pour l'infirmier ID: {}", id);

        List<PlanDeSoinsDTO> plansDeSoins = infirmierService.getInfirmierPlansDeSoins(id);
        return ResponseEntity.ok(plansDeSoins);
    }

    @GetMapping("/{id}/plans-de-soins/patient/{patientId}")
    @Operation(summary = "Récupérer les plans de soins d'un infirmier pour un patient",
            description = "Récupère tous les plans de soins d'un infirmier spécifique pour un patient donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans de soins récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Infirmier ou patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getInfirmierPlansDeSoinsByPatient(
            @Parameter(description = "ID de l'infirmier") @PathVariable Long id,
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de récupération des plans de soins pour l'infirmier ID: {} et le patient ID: {}", id, patientId);

        List<PlanDeSoinsDTO> plansDeSoins = infirmierService.getInfirmierPlansDeSoinsByPatient(id, patientId);
        return ResponseEntity.ok(plansDeSoins);
    }

    @GetMapping("/{id}/plans-de-soins/date/{date}")
    @Operation(summary = "Récupérer les plans de soins d'un infirmier par date",
            description = "Récupère tous les plans de soins d'un infirmier pour une date spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans de soins récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Infirmier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PlanDeSoinsDTO>> getInfirmierPlansDeSoinsByDate(
            @Parameter(description = "ID de l'infirmier") @PathVariable Long id,
            @Parameter(description = "Date des plans de soins (format: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Demande de récupération des plans de soins pour l'infirmier ID: {} à la date: {}", id, date);

        List<PlanDeSoinsDTO> plansDeSoins = infirmierService.getInfirmierPlansDeSoinsByDate(id, date);
        return ResponseEntity.ok(plansDeSoins);
    }

    @GetMapping("/{id}/patients")
    @Operation(summary = "Récupérer les patients assignés à un infirmier",
            description = "Récupère la liste de tous les patients sous la responsabilité d'un infirmier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Infirmier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PatientDTO>> getInfirmierPatients(
            @Parameter(description = "ID de l'infirmier") @PathVariable Long id) {
        log.info("Demande de récupération des patients pour l'infirmier ID: {}", id);

        List<PatientDTO> patients = infirmierService.getInfirmierPatients(id);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des infirmiers",
            description = "Effectue une recherche d'infirmiers par nom, prénom ou autres critères")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<InfirmierDTO>> searchInfirmiers(
            @Parameter(description = "Terme de recherche (nom, prénom, etc.)")
            @RequestParam String query) {
        log.info("Demande de recherche d'infirmiers avec le terme: {}", query);

        List<InfirmierDTO> infirmiers = infirmierService.searchInfirmiers(query);
        return ResponseEntity.ok(infirmiers);
    }

    @GetMapping("/niveau-qualification/{niveauQualification}")
    @Operation(summary = "Récupérer les infirmiers par niveau de qualification",
            description = "Récupère tous les infirmiers ayant un niveau de qualification spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Infirmiers récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<InfirmierDTO>> getInfirmiersByNiveauQualification(
            @Parameter(description = "Niveau de qualification recherché")
            @PathVariable String niveauQualification) {
        log.info("Demande de récupération des infirmiers avec le niveau de qualification: {}", niveauQualification);

        List<InfirmierDTO> infirmiers = infirmierService.getInfirmiersByNiveauQualification(niveauQualification);
        return ResponseEntity.ok(infirmiers);
    }
}