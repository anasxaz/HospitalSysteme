package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.service.CadreAdministratifService;
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
@RequestMapping("/api/cadres-administratifs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cadres Administratifs", description = "API pour la gestion des cadres administratifs et leurs activités")
public class CadreAdministratifController {

    private final CadreAdministratifService cadreAdministratifService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des cadres administratifs")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans CadreAdministratifController fonctionne");
    }

    // ====================== GESTION DES CADRES ADMINISTRATIFS ======================

    @PostMapping
    @Operation(summary = "Créer un nouveau cadre administratif",
            description = "Permet de créer un nouveau cadre administratif dans le système hospitalier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadre administratif créé avec succès",
                    content = @Content(schema = @Schema(implementation = CadreAdministratifDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou email déjà existant"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CadreAdministratifDTO> createCadreAdministratif(
            @Parameter(description = "Informations du cadre administratif à créer")
            @Valid @RequestBody CadreAdministratifCreationDTO cadreAdministratifCreationDTO) {
        log.info("Demande de création d'un cadre administratif avec l'email: {}", cadreAdministratifCreationDTO.getEmail());

        CadreAdministratifDTO createdCadre = cadreAdministratifService.createCadreAdministratif(cadreAdministratifCreationDTO);
        return new ResponseEntity<>(createdCadre, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un cadre administratif par ID",
            description = "Récupère les informations détaillées d'un cadre administratif par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadre administratif trouvé",
                    content = @Content(schema = @Schema(implementation = CadreAdministratifDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cadre administratif non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<CadreAdministratifDTO> getCadreAdministratifById(
            @Parameter(description = "ID du cadre administratif") @PathVariable Long id) {
        log.info("Demande de récupération du cadre administratif avec l'ID: {}", id);

        CadreAdministratifDTO cadreAdministratifDTO = cadreAdministratifService.getCadreAdministratifById(id);
        return ResponseEntity.ok(cadreAdministratifDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les cadres administratifs",
            description = "Récupère la liste complète de tous les cadres administratifs du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des cadres administratifs récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<CadreAdministratifDTO>> getAllCadresAdministratifs() {
        log.info("Demande de récupération de tous les cadres administratifs");

        List<CadreAdministratifDTO> cadresList = cadreAdministratifService.getAllCadresAdministratifs();
        return ResponseEntity.ok(cadresList);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un cadre administratif",
            description = "Supprime définitivement un cadre administratif du système hospitalier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cadre administratif supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Cadre administratif non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCadreAdministratif(
            @Parameter(description = "ID du cadre administratif à supprimer") @PathVariable Long id) {
        log.info("Demande de suppression du cadre administratif avec l'ID: {}", id);

        cadreAdministratifService.deleteCadreAdministratif(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cadreId}/departement/{departementId}")
    @Operation(summary = "Assigner un cadre administratif à un département",
            description = "Assigne un cadre administratif à un département spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assignation effectuée avec succès"),
            @ApiResponse(responseCode = "404", description = "Cadre administratif ou département non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignerCadreAdministratifADepartement(
            @Parameter(description = "ID du cadre administratif") @PathVariable Long cadreId,
            @Parameter(description = "ID du département") @PathVariable Long departementId) {
        log.info("Demande d'assignation du cadre administratif ID: {} au département ID: {}", cadreId, departementId);

        cadreAdministratifService.assignerCadreAdministratifADepartement(cadreId, departementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des cadres administratifs",
            description = "Effectue une recherche de cadres administratifs par nom, prénom ou autres critères")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<CadreAdministratifDTO>> searchCadresAdministratifs(
            @Parameter(description = "Terme de recherche (nom, prénom, fonction, etc.)")
            @RequestParam String query) {
        log.info("Demande de recherche de cadres administratifs avec le terme: {}", query);

        List<CadreAdministratifDTO> cadres = cadreAdministratifService.searchCadresAdministratifs(query);
        return ResponseEntity.ok(cadres);
    }

    // ====================== GESTION DES RENDEZ-VOUS ======================

    @PostMapping("/rendez-vous")
    @Operation(summary = "Créer un nouveau rendez-vous",
            description = "Permet à un cadre administratif de créer un nouveau rendez-vous pour un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rendez-vous créé avec succès",
                    content = @Content(schema = @Schema(implementation = RendezVousDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<RendezVousDTO> createRendezVous(
            @Parameter(description = "Informations du rendez-vous à créer")
            @Valid @RequestBody RendezVousCreationDTO rendezVousCreationDTO) {
        log.info("Demande de création d'un rendez-vous pour le patient ID: {}", rendezVousCreationDTO.getPatientId());

        RendezVousDTO createdRendezVous = cadreAdministratifService.createRendezVous(rendezVousCreationDTO);
        return new ResponseEntity<>(createdRendezVous, HttpStatus.CREATED);
    }

    @PutMapping("/rendez-vous/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'un rendez-vous",
            description = "Permet de modifier le statut d'un rendez-vous existant (PROGRAMME, CONFIRME, ANNULE, TERMINE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Statut mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "400", description = "Statut invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Void> updateRendezVousStatut(
            @Parameter(description = "ID du rendez-vous") @PathVariable Long id,
            @Parameter(description = "Nouveau statut (PROGRAMME, CONFIRME, ANNULE, TERMINE)")
            @RequestParam String statut) {
        log.info("Demande de mise à jour du statut du rendez-vous ID: {} vers: {}", id, statut);

        cadreAdministratifService.updateRendezVousStatut(id, statut);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rendez-vous/date/{date}")
    @Operation(summary = "Récupérer les rendez-vous par date",
            description = "Récupère tous les rendez-vous programmés pour une date spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<RendezVousDTO>> getRendezVousByDate(
            @Parameter(description = "Date des rendez-vous (format: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Demande de récupération des rendez-vous pour la date: {}", date);

        List<RendezVousDTO> rendezVousList = cadreAdministratifService.getRendezVousByDate(date);
        return ResponseEntity.ok(rendezVousList);
    }

    // ====================== GESTION DES FACTURES ======================

    @PostMapping("/factures")
    @Operation(summary = "Créer une nouvelle facture",
            description = "Permet à un cadre administratif de créer une nouvelle facture pour un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facture créée avec succès",
                    content = @Content(schema = @Schema(implementation = FactureDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<FactureDTO> createFacture(
            @Parameter(description = "Informations de la facture à créer")
            @Valid @RequestBody FactureCreationDTO factureCreationDTO) {
        log.info("Demande de création d'une facture pour le patient ID: {}", factureCreationDTO.getPatientId());

        FactureDTO createdFacture = cadreAdministratifService.createFacture(factureCreationDTO);
        return new ResponseEntity<>(createdFacture, HttpStatus.CREATED);
    }

    @PutMapping("/factures/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'une facture",
            description = "Permet de modifier le statut de paiement d'une facture (EN_ATTENTE, PAYEE, ANNULEE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Statut de la facture mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée"),
            @ApiResponse(responseCode = "400", description = "Statut invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Void> updateFactureStatut(
            @Parameter(description = "ID de la facture") @PathVariable Long id,
            @Parameter(description = "Nouveau statut de paiement (EN_ATTENTE, PAYEE, ANNULEE)")
            @RequestParam String statut) {
        log.info("Demande de mise à jour du statut de la facture ID: {} vers: {}", id, statut);

        cadreAdministratifService.updateFactureStatut(id, statut);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/factures/statut/{statut}")
    @Operation(summary = "Récupérer les factures par statut",
            description = "Récupère toutes les factures ayant un statut de paiement spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factures récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<FactureDTO>> getFacturesByStatut(
            @Parameter(description = "Statut de paiement (EN_ATTENTE, PAYEE, ANNULEE)")
            @PathVariable StatutPaiement statut) {
        log.info("Demande de récupération des factures avec le statut: {}", statut);

        List<FactureDTO> factures = cadreAdministratifService.getFacturesByStatut(statut);
        return ResponseEntity.ok(factures);
    }
}