package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.RendezVousCreationDTO;
import com.hospital.HospitalSysteme.dto.RendezVousDTO;
import com.hospital.HospitalSysteme.dto.RendezVousUpdateDTO;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import com.hospital.HospitalSysteme.service.RendezVousService;
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
@RequestMapping("/api/rendez-vous")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Rendez-vous", description = "API pour la gestion des rendez-vous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans RendezVousController fonctionne");
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau rendez-vous", description = "Permet de créer un nouveau rendez-vous dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rendez-vous créé",
                    content = @Content(schema = @Schema(implementation = RendezVousDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou conflit d'horaire")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<RendezVousDTO> createRendezVous(
            @Parameter(description = "Informations du rendez-vous à créer") @Valid @RequestBody RendezVousCreationDTO rendezVousCreationDTO) {
        log.info("Demande de création d'un rendez-vous pour le patient ID: {} avec le médecin ID: {}",
                rendezVousCreationDTO.getPatientId(), rendezVousCreationDTO.getMedecinId());
        RendezVousDTO createdRendezVous = rendezVousService.createRendezVous(rendezVousCreationDTO);
        return new ResponseEntity<>(createdRendezVous, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un rendez-vous par ID", description = "Récupère les informations d'un rendez-vous par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous trouvé"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<RendezVousDTO> getRendezVousById(
            @Parameter(description = "ID du rendez-vous") @PathVariable Long id) {
        log.info("Demande de récupération du rendez-vous avec l'ID: {}", id);
        RendezVousDTO rendezVousDTO = rendezVousService.getRendezVousById(id);
        return ResponseEntity.ok(rendezVousDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les rendez-vous", description = "Récupère la liste de tous les rendez-vous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<RendezVousDTO>> getAllRendezVous() {
        log.info("Demande de récupération de tous les rendez-vous");
        List<RendezVousDTO> rendezVousList = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(rendezVousList);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un rendez-vous", description = "Met à jour les informations d'un rendez-vous existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous mis à jour"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<RendezVousDTO> updateRendezVous(
            @Parameter(description = "ID du rendez-vous") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour") @Valid @RequestBody RendezVousUpdateDTO rendezVousUpdateDTO) {
        log.info("Demande de mise à jour du rendez-vous avec l'ID: {}", id);
        RendezVousDTO updatedRendezVous = rendezVousService.updateRendezVous(id, rendezVousUpdateDTO);
        return ResponseEntity.ok(updatedRendezVous);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un rendez-vous", description = "Supprime un rendez-vous du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rendez-vous supprimé"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Void> deleteRendezVous(
            @Parameter(description = "ID du rendez-vous") @PathVariable Long id) {
        log.info("Demande de suppression du rendez-vous avec l'ID: {}", id);
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'un rendez-vous",
            description = "Met à jour uniquement le statut d'un rendez-vous spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Statut mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Void> updateRendezVousStatut(
            @Parameter(description = "ID du rendez-vous") @PathVariable Long id,
            @Parameter(description = "Nouveau statut du rendez-vous") @RequestParam StatutRendezVous statut) {
        log.info("Demande de mise à jour du statut du rendez-vous ID: {} vers: {}", id, statut);
        rendezVousService.updateRendezVousStatut(id, statut);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les rendez-vous par statut",
            description = "Récupère tous les rendez-vous ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<RendezVousDTO>> getRendezVousByStatut(
            @Parameter(description = "Statut des rendez-vous à récupérer") @PathVariable StatutRendezVous statut) {
        log.info("Demande de récupération des rendez-vous avec le statut: {}", statut);
        List<RendezVousDTO> rendezVousList = rendezVousService.getRendezVousByStatut(statut);
        return ResponseEntity.ok(rendezVousList);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Récupérer les rendez-vous d'un patient",
            description = "Récupère tous les rendez-vous d'un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous du patient récupérée"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<List<RendezVousDTO>> getRendezVousByPatient(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de récupération des rendez-vous pour le patient ID: {}", patientId);
        List<RendezVousDTO> rendezVousList = rendezVousService.getRendezVousByPatient(patientId);
        return ResponseEntity.ok(rendezVousList);
    }

    @GetMapping("/medecin/{medecinId}")
    @Operation(summary = "Récupérer les rendez-vous d'un médecin",
            description = "Récupère tous les rendez-vous d'un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous du médecin récupérée"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<RendezVousDTO>> getRendezVousByMedecin(
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId) {
        log.info("Demande de récupération des rendez-vous pour le médecin ID: {}", medecinId);
        List<RendezVousDTO> rendezVousList = rendezVousService.getRendezVousByMedecin(medecinId);
        return ResponseEntity.ok(rendezVousList);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Récupérer les rendez-vous d'une date",
            description = "Récupère tous les rendez-vous d'une date spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous de la date récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<RendezVousDTO>> getRendezVousByDate(
            @Parameter(description = "Date des rendez-vous (format: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Demande de récupération des rendez-vous pour la date: {}", date);
        List<RendezVousDTO> rendezVousList = rendezVousService.getRendezVousByDate(date);
        return ResponseEntity.ok(rendezVousList);
    }

    @GetMapping("/periode")
    @Operation(summary = "Récupérer les rendez-vous entre deux dates",
            description = "Récupère tous les rendez-vous dans une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous de la période récupérée"),
            @ApiResponse(responseCode = "400", description = "Dates invalides")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<RendezVousDTO>> getRendezVousBetweenDates(
            @Parameter(description = "Date et heure de début (format: yyyy-MM-ddTHH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @Parameter(description = "Date et heure de fin (format: yyyy-MM-ddTHH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        log.info("Demande de récupération des rendez-vous entre {} et {}", debut, fin);
        List<RendezVousDTO> rendezVousList = rendezVousService.getRendezVousBetweenDates(debut, fin);
        return ResponseEntity.ok(rendezVousList);
    }

    @PostMapping("/{id}/rappel")
    @Operation(summary = "Envoyer un rappel de rendez-vous",
            description = "Envoie un rappel au patient pour son rendez-vous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rappel envoyé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "400", description = "Impossible d'envoyer le rappel (rendez-vous passé ou annulé)")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Void> envoyerRappelRendezVous(
            @Parameter(description = "ID du rendez-vous") @PathVariable Long id) {
        log.info("Demande d'envoi de rappel pour le rendez-vous ID: {}", id);
        rendezVousService.envoyerRappelRendezVous(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medecin/{medecinId}/count")
    @Operation(summary = "Compter les rendez-vous d'un médecin par statut",
            description = "Compte le nombre de rendez-vous d'un médecin avec un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de rendez-vous récupéré"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countRendezVousByMedecinAndStatut(
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId,
            @Parameter(description = "Statut des rendez-vous à compter") @RequestParam StatutRendezVous statut) {
        log.info("Demande de comptage des rendez-vous pour le médecin ID: {} avec le statut: {}", medecinId, statut);
        int count = rendezVousService.countRendezVousByMedecinAndStatut(medecinId, statut);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/patient/{patientId}/count")
    @Operation(summary = "Compter les rendez-vous d'un patient par statut",
            description = "Compte le nombre de rendez-vous d'un patient avec un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de rendez-vous récupéré"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF', 'PATIENT')")
    public ResponseEntity<Integer> countRendezVousByPatientAndStatut(
            @Parameter(description = "ID du patient") @PathVariable Long patientId,
            @Parameter(description = "Statut des rendez-vous à compter") @RequestParam StatutRendezVous statut) {
        log.info("Demande de comptage des rendez-vous pour le patient ID: {} avec le statut: {}", patientId, statut);
        int count = rendezVousService.countRendezVousByPatientAndStatut(patientId, statut);
        return ResponseEntity.ok(count);
    }
}