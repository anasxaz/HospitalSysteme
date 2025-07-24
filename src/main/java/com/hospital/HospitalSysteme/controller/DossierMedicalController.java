package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.DossierMedicalService;
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
@RequestMapping("/api/dossiers-medicaux")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dossiers Médicaux", description = "API pour la gestion des dossiers médicaux des patients")
public class DossierMedicalController {

    private final DossierMedicalService dossierMedicalService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des dossiers médicaux")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans DossierMedicalController fonctionne");
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau dossier médical", description = "Permet de créer un nouveau dossier médical pour un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dossier médical créé",
                    content = @Content(schema = @Schema(implementation = DossierMedicalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou patient déjà associé à un dossier médical"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
    public ResponseEntity<DossierMedicalDTO> createDossierMedical(
            @Parameter(description = "Informations du dossier médical à créer") @Valid @RequestBody DossierMedicalCreationDTO dossierMedicalCreationDTO) {
        log.info("Demande de création d'un dossier médical pour le patient ID: {}", dossierMedicalCreationDTO.getPatientId());

        DossierMedicalDTO createdDossierMedical = dossierMedicalService.createDossierMedical(dossierMedicalCreationDTO);
        return new ResponseEntity<>(createdDossierMedical, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un dossier médical par ID", description = "Récupère les informations d'un dossier médical par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical trouvé"),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<DossierMedicalDTO> getDossierMedicalById(
            @Parameter(description = "ID du dossier médical") @PathVariable Long id) {
        log.info("Demande de récupération du dossier médical avec l'ID: {}", id);

        DossierMedicalDTO dossierMedicalDTO = dossierMedicalService.getDossierMedicalById(id);
        return ResponseEntity.ok(dossierMedicalDTO);
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Récupérer les détails d'un dossier médical",
            description = "Récupère les informations détaillées d'un dossier médical, y compris les consultations et prescriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Détails du dossier médical trouvés"),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<DossierMedicalDetailDTO> getDossierMedicalDetailById(
            @Parameter(description = "ID du dossier médical") @PathVariable Long id) {
        log.info("Demande de récupération des détails du dossier médical avec l'ID: {}", id);

        DossierMedicalDetailDTO dossierMedicalDetailDTO = dossierMedicalService.getDossierMedicalDetailById(id);
        return ResponseEntity.ok(dossierMedicalDetailDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un dossier médical",
            description = "Met à jour les informations d'un dossier médical existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou nouveau patient déjà associé à un dossier médical"),
            @ApiResponse(responseCode = "404", description = "Dossier médical ou patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
    public ResponseEntity<DossierMedicalDTO> updateDossierMedical(
            @Parameter(description = "ID du dossier médical") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour") @Valid @RequestBody DossierMedicalCreationDTO dossierMedicalUpdateDTO) {
        log.info("Demande de mise à jour du dossier médical avec l'ID: {}", id);

        DossierMedicalDTO updatedDossierMedical = dossierMedicalService.updateDossierMedical(id, dossierMedicalUpdateDTO);
        return ResponseEntity.ok(updatedDossierMedical);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un dossier médical",
            description = "Supprime un dossier médical du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dossier médical supprimé"),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDossierMedical(
            @Parameter(description = "ID du dossier médical") @PathVariable Long id) {
        log.info("Demande de suppression du dossier médical avec l'ID: {}", id);

        dossierMedicalService.deleteDossierMedical(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Récupérer le dossier médical d'un patient",
            description = "Récupère le dossier médical associé à un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical du patient trouvé"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé ou dossier médical non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<DossierMedicalDTO> getDossierMedicalByPatientId(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de récupération du dossier médical pour le patient ID: {}", patientId);

        DossierMedicalDTO dossierMedicalDTO = dossierMedicalService.getDossierMedicalByPatientId(patientId);
        return ResponseEntity.ok(dossierMedicalDTO);
    }

    @GetMapping("/{id}/consultations")
    @Operation(summary = "Récupérer les consultations d'un dossier médical",
            description = "Récupère toutes les consultations associées à un dossier médical spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations récupérée"),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByDossierMedicalId(
            @Parameter(description = "ID du dossier médical") @PathVariable Long id) {
        log.info("Demande de récupération des consultations pour le dossier médical ID: {}", id);

        List<ConsultationDTO> consultations = dossierMedicalService.getConsultationsByDossierMedicalId(id);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/{id}/prescriptions")
    @Operation(summary = "Récupérer les prescriptions d'un dossier médical",
            description = "Récupère toutes les prescriptions associées à un dossier médical spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions récupérée"),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByDossierMedicalId(
            @Parameter(description = "ID du dossier médical") @PathVariable Long id) {
        log.info("Demande de récupération des prescriptions pour le dossier médical ID: {}", id);

        List<PrescriptionDTO> prescriptions = dossierMedicalService.getPrescriptionsByDossierMedicalId(id);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/count")
    @Operation(summary = "Compter le nombre total de dossiers médicaux",
            description = "Compte le nombre total de dossiers médicaux dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de dossiers médicaux récupéré")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countDossiersMedicaux() {
        log.info("Demande de comptage du nombre total de dossiers médicaux");

        int count = dossierMedicalService.countDossiersMedicaux();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des dossiers médicaux par nom de patient",
            description = "Recherche des dossiers médicaux en fonction du nom du patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des dossiers médicaux correspondants récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<DossierMedicalDTO>> searchDossiersMedicauxByPatientNom(
            @Parameter(description = "Nom du patient (recherche partielle)") @RequestParam String nom) {
        log.info("Demande de recherche de dossiers médicaux par nom de patient: {}", nom);

        List<DossierMedicalDTO> dossiersMedicaux = dossierMedicalService.searchDossiersMedicauxByPatientNom(nom);
        return ResponseEntity.ok(dossiersMedicaux);
    }

    @GetMapping("/groupe-sanguin/{groupeSanguin}")
    @Operation(summary = "Récupérer les dossiers médicaux par groupe sanguin",
            description = "Récupère tous les dossiers médicaux ayant un groupe sanguin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des dossiers médicaux récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<DossierMedicalDTO>> getDossiersMedicauxByGroupeSanguin(
            @Parameter(description = "Groupe sanguin (A+, A-, B+, B-, AB+, AB-, O+, O-)") @PathVariable String groupeSanguin) {
        log.info("Demande de récupération des dossiers médicaux avec le groupe sanguin: {}", groupeSanguin);

        List<DossierMedicalDTO> dossiersMedicaux = dossierMedicalService.getDossiersMedicauxByGroupeSanguin(groupeSanguin);
        return ResponseEntity.ok(dossiersMedicaux);
    }

    @GetMapping("/allergies")
    @Operation(summary = "Récupérer les dossiers médicaux avec certaines allergies",
            description = "Récupère tous les dossiers médicaux contenant certaines allergies spécifiques")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des dossiers médicaux récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<DossierMedicalDTO>> getDossiersMedicauxWithAllergies(
            @Parameter(description = "Allergies (recherche partielle)") @RequestParam String allergies) {
        log.info("Demande de récupération des dossiers médicaux avec les allergies: {}", allergies);

        List<DossierMedicalDTO> dossiersMedicaux = dossierMedicalService.getDossiersMedicauxWithAllergies(allergies);
        return ResponseEntity.ok(dossiersMedicaux);
    }
}