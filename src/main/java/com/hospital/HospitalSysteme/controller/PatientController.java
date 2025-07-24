package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.service.PatientService;
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
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patients", description = "API pour la gestion des patients")
public class PatientController {

    private final PatientService patientService;


    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans PatientController fonctionne");
    }



    @PostMapping("/register")
    @Operation(summary = "Inscrire un nouveau patient", description = "Permet à un patient de créer son compte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient créé",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou email déjà utilisé")
    })
    public ResponseEntity<PatientDTO> registerPatient(
            @Parameter(description = "Informations d'inscription du patient") @Valid @RequestBody PatientCreationDTO patientCreationDTO) {
        log.info("Demande d'inscription d'un nouveau patient avec l'email : {}", patientCreationDTO.getEmail());
        PatientDTO createdPatient = patientService.createPatient(patientCreationDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un patient par ID", description = "Récupère les informations d'un patient par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient trouvé"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<PatientDTO> getPatientById(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Récupération du patient avec l'ID : {}", id);
        PatientDTO patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Récupérer les détails d'un patient", description = "Récupère les informations détaillées d'un patient par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Détails du patient trouvés"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<PatientDetailDTO> getPatientDetailsById(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Récupération des détails du patient avec l'ID : {}", id);
        PatientDetailDTO patientDetails = patientService.getPatientDetailsById(id);
        return ResponseEntity.ok(patientDetails);
    }

    @GetMapping("/{id}/medical-summary")
    @Operation(summary = "Récupérer le résumé médical d'un patient", description = "Récupère le résumé médical d'un patient par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résumé médical trouvé"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<PatientMedicalSummaryDTO> getPatientMedicalSummary(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Récupération du résumé médical du patient avec l'ID : {}", id);
        PatientMedicalSummaryDTO medicalSummary = patientService.getPatientMedicalSummary(id);
        return ResponseEntity.ok(medicalSummary);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les patients", description = "Récupère la liste de tous les patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        log.info("Récupération de tous les patients");
        List<PatientDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un patient", description = "Met à jour les informations d'un patient existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient mis à jour"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<PatientDTO> updatePatient(
            @Parameter(description = "ID du patient") @PathVariable Long id,
            @Parameter(description = "Informations mises à jour du patient") @Valid @RequestBody PatientUpdateDTO patientUpdateDTO) {
        log.info("Mise à jour du patient avec l'ID : {}", id);
        PatientDTO updatedPatient = patientService.updatePatient(id, patientUpdateDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un patient", description = "Supprime un patient du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient supprimé"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Suppression du patient avec l'ID : {}", id);
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/dossier-medical")
    @Operation(summary = "Récupérer le dossier médical d'un patient", description = "Récupère le dossier médical d'un patient par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical trouvé"),
            @ApiResponse(responseCode = "404", description = "Patient ou dossier médical non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<DossierMedicalDTO> getPatientDossierMedical(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Récupération du dossier médical du patient avec l'ID : {}", id);
        DossierMedicalDTO dossierMedical = patientService.getPatientDossierMedical(id);
        return ResponseEntity.ok(dossierMedical);
    }

    @GetMapping("/{id}/rendez-vous")
    @Operation(summary = "Récupérer les rendez-vous d'un patient", description = "Récupère tous les rendez-vous d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getPatientRendezVous(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Récupération des rendez-vous du patient avec l'ID : {}", id);
        List<RendezVousDTO> rendezVous = patientService.getPatientRendezVous(id);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/rendez-vous/a-venir")
    @Operation(summary = "Récupérer les rendez-vous à venir d'un patient", description = "Récupère les rendez-vous à venir d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous à venir récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getPatientRendezVousAVenir(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Récupération des rendez-vous à venir du patient avec l'ID : {}", id);
        List<RendezVousDTO> rendezVous = patientService.getPatientRendezVousAVenir(id);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/rendez-vous/passes")
    @Operation(summary = "Récupérer les rendez-vous passés d'un patient", description = "Récupère les rendez-vous passés d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous passés récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getPatientRendezVousPasses(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Récupération des rendez-vous passés du patient avec l'ID : {}", id);
        List<RendezVousDTO> rendezVous = patientService.getPatientRendezVousPasses(id);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/rendez-vous/medecin/{medecinId}")
    @Operation(summary = "Récupérer les rendez-vous d'un patient avec un médecin spécifique",
            description = "Récupère les rendez-vous d'un patient avec un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getPatientRendezVousByMedecin(
            @Parameter(description = "ID du patient") @PathVariable Long id,
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId) {
        log.info("Récupération des rendez-vous du patient avec l'ID : {} pour le médecin avec l'ID : {}", id, medecinId);
        List<RendezVousDTO> rendezVous = patientService.getPatientRendezVousByMedecin(id, medecinId);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/factures")
    @Operation(summary = "Récupérer les factures d'un patient", description = "Récupère toutes les factures d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des factures récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<FactureDTO>> getPatientFactures(
            @Parameter(description = "ID du patient") @PathVariable Long id) {
        log.info("Récupération des factures du patient avec l'ID : {}", id);
        List<FactureDTO> factures = patientService.getPatientFactures(id);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/{id}/factures/statut/{statut}")
    @Operation(summary = "Récupérer les factures d'un patient par statut",
            description = "Récupère les factures d'un patient filtrées par statut de paiement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des factures récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<FactureDTO>> getPatientFacturesByStatut(
            @Parameter(description = "ID du patient") @PathVariable Long id,
            @Parameter(description = "Statut de paiement") @PathVariable StatutPaiement statut) {
        log.info("Récupération des factures du patient avec l'ID : {} et le statut : {}", id, statut);
        List<FactureDTO> factures = patientService.getPatientFacturesByStatut(id, statut);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des patients", description = "Recherche des patients par nom, prénom, email ou numéro d'assurance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients correspondants récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PatientDTO>> searchPatients(
            @Parameter(description = "Terme de recherche") @RequestParam String query) {
        log.info("Recherche de patients avec le terme : {}", query);
        List<PatientDTO> patients = patientService.searchPatients(query);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/groupe-sanguin/{groupeSanguin}")
    @Operation(summary = "Récupérer les patients par groupe sanguin",
            description = "Récupère tous les patients ayant un groupe sanguin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PatientDTO>> getPatientsByGroupeSanguin(
            @Parameter(description = "Groupe sanguin") @PathVariable GroupeSanguin groupeSanguin) {
        log.info("Récupération des patients avec le groupe sanguin : {}", groupeSanguin);
        List<PatientDTO> patients = patientService.getPatientsByGroupeSanguin(groupeSanguin);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/allergies")
    @Operation(summary = "Récupérer les patients par allergies",
            description = "Récupère tous les patients ayant une allergie spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<PatientDTO>> getPatientsByAllergies(
            @Parameter(description = "Allergie") @RequestParam String allergies) {
        log.info("Récupération des patients avec l'allergie : {}", allergies);
        List<PatientDTO> patients = patientService.getPatientsByAllergies(allergies);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/numero-assurance/{numeroAssurance}")
    @Operation(summary = "Récupérer un patient par numéro d'assurance",
            description = "Récupère un patient par son numéro d'assurance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient trouvé"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<PatientDTO> getPatientByNumeroAssurance(
            @Parameter(description = "Numéro d'assurance") @PathVariable String numeroAssurance) {
        log.info("Récupération du patient avec le numéro d'assurance : {}", numeroAssurance);
        PatientDTO patient = patientService.getPatientByNumeroAssurance(numeroAssurance);
        return ResponseEntity.ok(patient);
    }
}