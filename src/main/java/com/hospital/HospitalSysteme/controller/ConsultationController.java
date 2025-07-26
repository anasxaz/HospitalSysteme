package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.ConsultationService;
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
import java.util.Map;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Consultations", description = "API pour la gestion des consultations médicales")
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des consultations")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans ConsultationController fonctionne");
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle consultation", description = "Permet de créer une nouvelle consultation médicale dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consultation créée",
                    content = @Content(schema = @Schema(implementation = ConsultationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
    public ResponseEntity<ConsultationDTO> createConsultation(
            @Parameter(description = "Informations de la consultation à créer") @Valid @RequestBody ConsultationCreationDTO consultationCreationDTO) {
        log.info("Demande de création d'une consultation par le médecin ID: {} pour le dossier médical ID: {}",
                consultationCreationDTO.getMedecinId(), consultationCreationDTO.getDossierMedicalId());

        ConsultationDTO createdConsultation = consultationService.createConsultation(consultationCreationDTO);
        return new ResponseEntity<>(createdConsultation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une consultation par ID", description = "Récupère les informations d'une consultation médicale par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultation trouvée"),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<ConsultationDTO> getConsultationById(
            @Parameter(description = "ID de la consultation") @PathVariable Long id) {
        log.info("Demande de récupération de la consultation avec l'ID: {}", id);

        ConsultationDTO consultationDTO = consultationService.getConsultationById(id);
        return ResponseEntity.ok(consultationDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les consultations", description = "Récupère la liste de toutes les consultations médicales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<ConsultationDTO>> getAllConsultations() {
        log.info("Demande de récupération de toutes les consultations");

        List<ConsultationDTO> consultationList = consultationService.getAllConsultations();
        return ResponseEntity.ok(consultationList);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une consultation", description = "Met à jour les informations d'une consultation médicale existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultation mise à jour"),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
    public ResponseEntity<ConsultationDTO> updateConsultation(
            @Parameter(description = "ID de la consultation") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour") @Valid @RequestBody ConsultationUpdateDTO consultationUpdateDTO) {
        log.info("Demande de mise à jour de la consultation avec l'ID: {}", id);

        ConsultationDTO updatedConsultation = consultationService.updateConsultation(id, consultationUpdateDTO);
        return ResponseEntity.ok(updatedConsultation);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une consultation", description = "Supprime une consultation médicale du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consultation supprimée"),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteConsultation(
            @Parameter(description = "ID de la consultation") @PathVariable Long id) {
        log.info("Demande de suppression de la consultation avec l'ID: {}", id);

        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Récupérer les consultations d'un patient",
            description = "Récupère toutes les consultations d'un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations du patient récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByPatient(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de récupération des consultations pour le patient ID: {}", patientId);

        List<ConsultationDTO> consultations = consultationService.getConsultationsByPatient(patientId);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/patient/{patientId}/summary")
    @Operation(summary = "Récupérer les résumés de consultations d'un patient",
            description = "Récupère un résumé de toutes les consultations d'un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des résumés de consultations du patient récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<ConsultationSummaryDTO>> getConsultationsSummaryByPatient(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de récupération des résumés de consultations pour le patient ID: {}", patientId);

        List<ConsultationSummaryDTO> consultationSummaries = consultationService.getConsultationsSummaryByPatient(patientId);
        return ResponseEntity.ok(consultationSummaries);
    }

    @GetMapping("/medecin/{medecinId}")
    @Operation(summary = "Récupérer les consultations d'un médecin",
            description = "Récupère toutes les consultations effectuées par un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations du médecin récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByMedecin(
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId) {
        log.info("Demande de récupération des consultations pour le médecin ID: {}", medecinId);

        List<ConsultationDTO> consultations = consultationService.getConsultationsByMedecin(medecinId);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Récupérer les consultations d'une date",
            description = "Récupère toutes les consultations effectuées à une date spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations de la date récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByDate(
            @Parameter(description = "Date des consultations (format: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Demande de récupération des consultations pour la date: {}", date);

        List<ConsultationDTO> consultations = consultationService.getConsultationsByDate(date);
        return ResponseEntity.ok(consultations);
    }

    @PostMapping("/{id}/notes")
    @Operation(summary = "Ajouter une note à une consultation",
            description = "Ajoute une note médicale à une consultation existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Note ajoutée avec succès"),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée")
    })
    @PreAuthorize("hasAnyRole('MEDECIN', 'PERSONNEL')")
    public ResponseEntity<Void> ajouterNoteConsultation(
            @PathVariable Long id,
            @RequestParam("note") String note) { // ✅ Plus simple !

        log.info("Demande d'ajout de note à la consultation ID: {}", id);

        if (note == null || note.trim().isEmpty()) {
            throw new IllegalArgumentException("La note ne peut pas être vide");
        }

        consultationService.ajouterNoteConsultation(id, note);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/diagnostic")
    @Operation(summary = "Ajouter un diagnostic à une consultation",
            description = "Ajoute ou met à jour le diagnostic médical d'une consultation existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Diagnostic ajouté avec succès"),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée")
    })
    @PreAuthorize("hasAnyRole('MEDECIN', 'PERSONNEL')")
    public ResponseEntity<Void> ajouterDiagnostic(
            @PathVariable Long id,
            @RequestParam("diagnostic") String diagnostic) { // ✅ Plus simple !

        log.info("Demande d'ajout de diagnostic à la consultation ID: {}", id);

        if (diagnostic == null || diagnostic.trim().isEmpty()) {
            throw new IllegalArgumentException("Le diagnostic ne peut pas être vide");
        }

        consultationService.ajouterDiagnostic(id, diagnostic);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/prescriptions")
    @Operation(summary = "Récupérer les prescriptions d'une consultation",
            description = "Récupère toutes les prescriptions médicales associées à une consultation spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions récupérée"),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByConsultation(
            @Parameter(description = "ID de la consultation") @PathVariable Long id) {
        log.info("Demande de récupération des prescriptions pour la consultation ID: {}", id);

        List<PrescriptionDTO> prescriptions = consultationService.getPrescriptionsByConsultation(id);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/medecin/{medecinId}/count")
    @Operation(summary = "Compter les consultations d'un médecin",
            description = "Compte le nombre total de consultations effectuées par un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de consultations récupéré"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countConsultationsByMedecin(
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId) {
        log.info("Demande de comptage des consultations pour le médecin ID: {}", medecinId);

        int count = consultationService.countConsultationsByMedecin(medecinId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/patient/{patientId}/count")
    @Operation(summary = "Compter les consultations d'un patient",
            description = "Compte le nombre total de consultations pour un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de consultations récupéré"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countConsultationsByPatient(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de comptage des consultations pour le patient ID: {}", patientId);

        int count = consultationService.countConsultationsByPatient(patientId);
        return ResponseEntity.ok(count);
    }
}