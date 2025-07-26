package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.PrescriptionService;
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
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Prescriptions", description = "API pour la gestion des prescriptions médicales")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des prescriptions")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans PrescriptionController fonctionne");
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle prescription", description = "Permet de créer une nouvelle prescription médicale dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prescription créée",
                    content = @Content(schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Consultation ou médicament non trouvé")
    })
    @PreAuthorize("hasAnyRole('MEDECIN', 'PERSONNEL')")
    public ResponseEntity<PrescriptionDTO> createPrescription(
            @Parameter(description = "Informations de la prescription à créer") @Valid @RequestBody PrescriptionCreationDTO prescriptionCreationDTO) {
        log.info("Demande de création d'une prescription pour la consultation ID: {} avec le médicament ID: {}",
                prescriptionCreationDTO.getConsultationId(), prescriptionCreationDTO.getMedicamentId());

        PrescriptionDTO createdPrescription = prescriptionService.createPrescription(prescriptionCreationDTO);
        return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une prescription par ID", description = "Récupère les informations d'une prescription médicale par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription trouvée"),
            @ApiResponse(responseCode = "404", description = "Prescription non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN', 'PERSONNEL')")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(
            @Parameter(description = "ID de la prescription") @PathVariable Long id) {
        log.info("Demande de récupération de la prescription avec l'ID: {}", id);

        PrescriptionDTO prescriptionDTO = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescriptionDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les prescriptions", description = "Récupère la liste de toutes les prescriptions médicales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PHARMACIEN', 'PERSONNEL')")
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions() {
        log.info("Demande de récupération de toutes les prescriptions");

        List<PrescriptionDTO> prescriptionList = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptionList);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une prescription", description = "Met à jour les informations d'une prescription médicale existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription mise à jour"),
            @ApiResponse(responseCode = "404", description = "Prescription non trouvée")
    })
    @PreAuthorize("hasRole('MEDECIN', 'PERSONNEL')")
    public ResponseEntity<PrescriptionDTO> updatePrescription(
            @Parameter(description = "ID de la prescription") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour") @Valid @RequestBody PrescriptionUpdateDTO prescriptionUpdateDTO) {
        log.info("Demande de mise à jour de la prescription avec l'ID: {}", id);

        PrescriptionDTO updatedPrescription = prescriptionService.updatePrescription(id, prescriptionUpdateDTO);
        return ResponseEntity.ok(updatedPrescription);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une prescription", description = "Supprime une prescription médicale du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescription supprimée"),
            @ApiResponse(responseCode = "404", description = "Prescription non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PERSONNEL')")
    public ResponseEntity<Void> deletePrescription(
            @Parameter(description = "ID de la prescription") @PathVariable Long id) {
        log.info("Demande de suppression de la prescription avec l'ID: {}", id);

        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Récupérer les prescriptions d'un patient",
            description = "Récupère toutes les prescriptions médicales d'un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions du patient récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN', 'PERSONNEL')")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByPatient(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de récupération des prescriptions pour le patient ID: {}", patientId);

        List<PrescriptionDTO> prescriptions = prescriptionService.getPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/medecin/{medecinId}")
    @Operation(summary = "Récupérer les prescriptions d'un médecin",
            description = "Récupère toutes les prescriptions médicales prescrites par un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions du médecin récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PERSONNEL')")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByMedecin(
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId) {
        log.info("Demande de récupération des prescriptions pour le médecin ID: {}", medecinId);

        List<PrescriptionDTO> prescriptions = prescriptionService.getPrescriptionsByMedecin(medecinId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/consultation/{consultationId}")
    @Operation(summary = "Récupérer les prescriptions d'une consultation",
            description = "Récupère toutes les prescriptions médicales associées à une consultation spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions de la consultation récupérée"),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PERSONNEL')")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByConsultation(
            @Parameter(description = "ID de la consultation") @PathVariable Long consultationId) {
        log.info("Demande de récupération des prescriptions pour la consultation ID: {}", consultationId);

        List<PrescriptionDTO> prescriptions = prescriptionService.getPrescriptionsByConsultation(consultationId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Récupérer les prescriptions d'une date",
            description = "Récupère toutes les prescriptions médicales créées à une date spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions de la date récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PHARMACIEN', 'PERSONNEL')")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByDate(
            @Parameter(description = "Date des prescriptions (format: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Demande de récupération des prescriptions pour la date: {}", date);

        List<PrescriptionDTO> prescriptions = prescriptionService.getPrescriptionsByDate(date);
        return ResponseEntity.ok(prescriptions);
    }

    @PostMapping("/{id}/medicaments")
    @Operation(summary = "Ajouter un médicament à une prescription",
            description = "Ajoute un médicament à une prescription médicale existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médicament ajouté avec succès"),
            @ApiResponse(responseCode = "404", description = "Prescription non trouvée")
    })
    @PreAuthorize("hasAnyRole('MEDECIN', 'PERSONNEL')")
    public ResponseEntity<Void> ajouterMedicamentAPrescription(
            @Parameter(description = "ID de la prescription") @PathVariable Long id,
            @Parameter(description = "Médicament à ajouter") @Valid @RequestBody MedicamentDTO medicamentDTO) {
        log.info("Demande d'ajout d'un médicament à la prescription ID: {}", id);

        prescriptionService.ajouterMedicamentAPrescription(id, medicamentDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{prescriptionId}/medicaments/{medicamentId}")
    @Operation(summary = "Supprimer un médicament d'une prescription",
            description = "Retire un médicament d'une prescription médicale existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médicament supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Prescription ou médicament non trouvé")
    })
    @PreAuthorize("hasRole('MEDECIN', 'PERSONNEL')")
    public ResponseEntity<Void> supprimerMedicamentDePrescription(
            @Parameter(description = "ID de la prescription") @PathVariable Long prescriptionId,
            @Parameter(description = "ID du médicament") @PathVariable Long medicamentId) {
        log.info("Demande de suppression du médicament ID: {} de la prescription ID: {}", medicamentId, prescriptionId);

        prescriptionService.supprimerMedicamentDePrescription(prescriptionId, medicamentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/renouveler")
    @Operation(summary = "Renouveler une prescription",
            description = "Crée une nouvelle prescription basée sur une prescription existante avec une nouvelle durée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription renouvelée",
                    content = @Content(schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Durée invalide"),
            @ApiResponse(responseCode = "404", description = "Prescription non trouvée")
    })
    @PreAuthorize("hasAnyRole('MEDECIN', 'PERSONNEL')")
    public ResponseEntity<PrescriptionDTO> renouvelerPrescription(
            @Parameter(description = "ID de la prescription") @PathVariable Long id,
            @Parameter(description = "Durée en jours du renouvellement") @RequestParam int dureeJours) {
        log.info("Demande de renouvellement de la prescription ID: {} pour {} jours", id, dureeJours);

        PrescriptionDTO renewedPrescription = prescriptionService.renouvelerPrescription(id, dureeJours);
        return ResponseEntity.ok(renewedPrescription);
    }

    @GetMapping("/medecin/{medecinId}/count")
    @Operation(summary = "Compter les prescriptions d'un médecin",
            description = "Compte le nombre total de prescriptions faites par un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de prescriptions récupéré"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PERSONNEL')")
    public ResponseEntity<Integer> countPrescriptionsByMedecin(
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId) {
        log.info("Demande de comptage des prescriptions pour le médecin ID: {}", medecinId);

        int count = prescriptionService.countPrescriptionsByMedecin(medecinId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/patient/{patientId}/count")
    @Operation(summary = "Compter les prescriptions d'un patient",
            description = "Compte le nombre total de prescriptions pour un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de prescriptions récupéré"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PERSONNEL')")
    public ResponseEntity<Integer> countPrescriptionsByPatient(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de comptage des prescriptions pour le patient ID: {}", patientId);

        int count = prescriptionService.countPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/medicaments/plus-prescrits")
    @Operation(summary = "Récupérer les médicaments les plus prescrits",
            description = "Récupère la liste des médicaments les plus fréquemment prescrits avec leurs statistiques")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médicaments les plus prescrits récupérée"),
            @ApiResponse(responseCode = "400", description = "Limite invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PHARMACIEN', 'PERSONNEL')")
    public ResponseEntity<List<MedicamentStatDTO>> getMedicamentsLesPlusPrescrits(
            @Parameter(description = "Nombre de médicaments à récupérer") @RequestParam(defaultValue = "10") int limit) {
        log.info("Demande de récupération des {} médicaments les plus prescrits", limit);

        List<MedicamentStatDTO> medicaments = prescriptionService.getMedicamentsLesPlusPrescrits(limit);
        return ResponseEntity.ok(medicaments);
    }
}