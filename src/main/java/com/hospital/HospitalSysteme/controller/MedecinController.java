package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.MedecinService;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/medecins")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Médecins", description = "API pour la gestion des médecins")
public class MedecinController {

    private final MedecinService medecinService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans MedecinController fonctionne");
    }

    // Juste pour tester
    @GetMapping("/public-test")
    @Operation(summary = "Public test endpoint", description = "Endpoint de test public sans sécurité")
    public ResponseEntity<String> publicTestEndpoint() {
        return ResponseEntity.ok("Public test endpoint dans MedecinController fonctionne");
    }

    // Juste pour tester
    @PostMapping("/test-create")
    @Operation(summary = "Test de création d'un médecin", description = "Endpoint de test pour la création d'un médecin")
    public ResponseEntity<String> testCreateMedecin(@RequestBody MedecinCreationDTO dto) {
        try {
            // Afficher les informations du DTO
            return ResponseEntity.ok("DTO reçu avec succès : " + dto.getEmail() + ", Département ID : " + dto.getDepartementId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau médecin", description = "Permet de créer un nouveau médecin dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médecin créé",
                    content = @Content(schema = @Schema(implementation = MedecinDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou email déjà utilisé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedecinDTO> createMedecin(
            @Parameter(description = "Informations du médecin à créer") @Valid @RequestBody MedecinCreationDTO medecinCreationDTO) {
        log.info("Demande de création d'un nouveau médecin avec l'email : {}", medecinCreationDTO.getEmail());
        MedecinDTO createdMedecin = medecinService.createMedecin(medecinCreationDTO);
        return new ResponseEntity<>(createdMedecin, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un médecin par ID", description = "Récupère les informations d'un médecin par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médecin trouvé"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PATIENT') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<MedecinDTO> getMedecinById(
            @Parameter(description = "ID du médecin") @PathVariable Long id) {
        log.info("Récupération du médecin avec l'ID : {}", id);
        MedecinDTO medecin = medecinService.getMedecinById(id);
        return ResponseEntity.ok(medecin);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les médecins", description = "Récupère la liste de tous les médecins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médecins récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<MedecinDTO>> getAllMedecins() {
        log.info("Récupération de tous les médecins");
        List<MedecinDTO> medecins = medecinService.getAllMedecins();
        return ResponseEntity.ok(medecins);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un médecin", description = "Supprime un médecin du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médecin supprimé"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedecin(
            @Parameter(description = "ID du médecin") @PathVariable Long id) {
        log.info("Suppression du médecin avec l'ID : {}", id);
        medecinService.deleteMedecin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/rendez-vous")
    @Operation(summary = "Récupérer les rendez-vous d'un médecin", description = "Récupère tous les rendez-vous d'un médecin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getMedecinRendezVous(
            @Parameter(description = "ID du médecin") @PathVariable Long id) {
        log.info("Récupération des rendez-vous du médecin avec l'ID : {}", id);
        List<RendezVousDTO> rendezVous = medecinService.getMedecinRendezVous(id);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/rendez-vous/a-venir")
    @Operation(summary = "Récupérer les rendez-vous à venir d'un médecin", description = "Récupère les rendez-vous à venir d'un médecin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous à venir récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getMedecinRendezVousAVenir(
            @Parameter(description = "ID du médecin") @PathVariable Long id) {
        log.info("Récupération des rendez-vous à venir du médecin avec l'ID : {}", id);
        List<RendezVousDTO> rendezVous = medecinService.getMedecinRendezVousAVenir(id);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/rendez-vous/passes")
    @Operation(summary = "Récupérer les rendez-vous passés d'un médecin", description = "Récupère les rendez-vous passés d'un médecin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous passés récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getMedecinRendezVousPasses(
            @Parameter(description = "ID du médecin") @PathVariable Long id) {
        log.info("Récupération des rendez-vous passés du médecin avec l'ID : {}", id);
        List<RendezVousDTO> rendezVous = medecinService.getMedecinRendezVousPasses(id);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/rendez-vous/date/{date}")
    @Operation(summary = "Récupérer les rendez-vous d'un médecin à une date spécifique",
            description = "Récupère les rendez-vous d'un médecin pour une date spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getMedecinRendezVousByDate(
            @Parameter(description = "ID du médecin") @PathVariable Long id,
            @Parameter(description = "Date (format: yyyy-MM-dd)") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Récupération des rendez-vous du médecin avec l'ID : {} pour la date : {}", id, date);
        List<RendezVousDTO> rendezVous = medecinService.getMedecinRendezVousByDate(id, date);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/rendez-vous/patient/{patientId}")
    @Operation(summary = "Récupérer les rendez-vous d'un médecin avec un patient spécifique",
            description = "Récupère les rendez-vous d'un médecin avec un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin ou patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<RendezVousDTO>> getMedecinRendezVousByPatient(
            @Parameter(description = "ID du médecin") @PathVariable Long id,
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Récupération des rendez-vous du médecin avec l'ID : {} pour le patient avec l'ID : {}", id, patientId);
        List<RendezVousDTO> rendezVous = medecinService.getMedecinRendezVousByPatient(id, patientId);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}/consultations")
    @Operation(summary = "Récupérer les consultations d'un médecin", description = "Récupère toutes les consultations d'un médecin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<ConsultationDTO>> getMedecinConsultations(
            @Parameter(description = "ID du médecin") @PathVariable Long id) {
        log.info("Récupération des consultations du médecin avec l'ID : {}", id);
        List<ConsultationDTO> consultations = medecinService.getMedecinConsultations(id);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/{id}/consultations/patient/{patientId}")
    @Operation(summary = "Récupérer les consultations d'un médecin avec un patient spécifique",
            description = "Récupère les consultations d'un médecin avec un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin ou patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<ConsultationDTO>> getMedecinConsultationsByPatient(
            @Parameter(description = "ID du médecin") @PathVariable Long id,
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Récupération des consultations du médecin avec l'ID : {} pour le patient avec l'ID : {}", id, patientId);
        List<ConsultationDTO> consultations = medecinService.getMedecinConsultationsByPatient(id, patientId);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/{id}/consultations/periode")
    @Operation(summary = "Récupérer les consultations d'un médecin sur une période",
            description = "Récupère les consultations d'un médecin entre deux dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<ConsultationDTO>> getMedecinConsultationsByPeriode(
            @Parameter(description = "ID du médecin") @PathVariable Long id,
            @Parameter(description = "Date de début (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        log.info("Récupération des consultations du médecin avec l'ID : {} entre {} et {}", id, debut, fin);
        List<ConsultationDTO> consultations = medecinService.getMedecinConsultationsByPeriode(id, debut, fin);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/{id}/prescriptions")
    @Operation(summary = "Récupérer les prescriptions d'un médecin", description = "Récupère toutes les prescriptions d'un médecin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<PrescriptionDTO>> getMedecinPrescriptions(
            @Parameter(description = "ID du médecin") @PathVariable Long id) {
        log.info("Récupération des prescriptions du médecin avec l'ID : {}", id);
        List<PrescriptionDTO> prescriptions = medecinService.getMedecinPrescriptions(id);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/{id}/prescriptions/patient/{patientId}")
    @Operation(summary = "Récupérer les prescriptions d'un médecin pour un patient spécifique",
            description = "Récupère les prescriptions d'un médecin pour un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prescriptions récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin ou patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<List<PrescriptionDTO>> getMedecinPrescriptionsByPatient(
            @Parameter(description = "ID du médecin") @PathVariable Long id,
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Récupération des prescriptions du médecin avec l'ID : {} pour le patient avec l'ID : {}", id, patientId);
        List<PrescriptionDTO> prescriptions = medecinService.getMedecinPrescriptionsByPatient(id, patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des médecins", description = "Recherche des médecins par nom, prénom, email ou spécialité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médecins correspondants récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<MedecinDTO>> searchMedecins(
            @Parameter(description = "Terme de recherche") @RequestParam String query) {
        log.info("Recherche de médecins avec le terme : {}", query);
        List<MedecinDTO> medecins = medecinService.searchMedecins(query);
        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/specialite/{specialite}")
    @Operation(summary = "Récupérer les médecins par spécialité",
            description = "Récupère tous les médecins ayant une spécialité spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médecins récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<MedecinDTO>> getMedecinsBySpecialite(
            @Parameter(description = "Spécialité") @PathVariable String specialite) {
        log.info("Récupération des médecins avec la spécialité : {}", specialite);
        List<MedecinDTO> medecins = medecinService.getMedecinsBySpecialite(specialite);
        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Récupérer les médecins disponibles à une date et heure spécifique",
            description = "Récupère tous les médecins disponibles à une date et heure spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médecins disponibles récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<MedecinDTO>> getMedecinsByDisponibilite(
            @Parameter(description = "Date et heure (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateHeure) {
        log.info("Récupération des médecins disponibles à la date et heure : {}", dateHeure);
        List<MedecinDTO> medecins = medecinService.getMedecinsByDisponibilite(dateHeure);
        return ResponseEntity.ok(medecins);
    }
}