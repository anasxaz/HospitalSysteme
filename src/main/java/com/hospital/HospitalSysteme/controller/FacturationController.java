package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.service.FacturationService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Facturation", description = "API pour la gestion de la facturation hospitalière, paiements et analyses financières")
public class FacturationController {

    private final FacturationService facturationService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API de facturation")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans FacturationController fonctionne");
    }

    // ====================== GESTION CRUD DES FACTURES ======================

    @PostMapping("/factures")
    @Operation(summary = "Créer une nouvelle facture",
            description = "Permet de créer une nouvelle facture pour un patient avec les services associés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facture créée avec succès",
                    content = @Content(schema = @Schema(implementation = FactureDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou montant incorrect"),
            @ApiResponse(responseCode = "404", description = "Patient, cadre administratif ou service non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<FactureDTO> createFacture(
            @Parameter(description = "Informations de la facture à créer")
            @Valid @RequestBody FactureCreationDTO factureCreationDTO) {
        log.info("Demande de création d'une facture pour le patient ID: {} d'un montant de: {}",
                factureCreationDTO.getPatientId(), factureCreationDTO.getMontantTotal());

        FactureDTO createdFacture = facturationService.createFacture(factureCreationDTO);
        return new ResponseEntity<>(createdFacture, HttpStatus.CREATED);
    }

    @GetMapping("/factures/{id}")
    @Operation(summary = "Récupérer une facture par ID",
            description = "Récupère les informations d'une facture par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facture trouvée",
                    content = @Content(schema = @Schema(implementation = FactureDTO.class))),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<FactureDTO> getFactureById(
            @Parameter(description = "ID de la facture") @PathVariable Long id) {
        log.info("Demande de récupération de la facture avec l'ID: {}", id);

        FactureDTO factureDTO = facturationService.getFactureById(id);
        return ResponseEntity.ok(factureDTO);
    }

    @GetMapping("/factures/{id}/detail")
    @Operation(summary = "Récupérer le détail complet d'une facture",
            description = "Récupère les informations détaillées d'une facture avec tous les services associés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Détail de la facture récupéré",
                    content = @Content(schema = @Schema(implementation = FactureDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<FactureDetailDTO> getFactureDetailById(
            @Parameter(description = "ID de la facture") @PathVariable Long id) {
        log.info("Demande de récupération du détail de la facture avec l'ID: {}", id);

        FactureDetailDTO factureDetailDTO = facturationService.getFactureDetailById(id);
        return ResponseEntity.ok(factureDetailDTO);
    }

    @GetMapping("/factures")
    @Operation(summary = "Récupérer toutes les factures",
            description = "Récupère la liste complète de toutes les factures du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des factures récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<FactureDTO>> getAllFactures() {
        log.info("Demande de récupération de toutes les factures");

        List<FactureDTO> facturesList = facturationService.getAllFactures();
        return ResponseEntity.ok(facturesList);
    }

    @DeleteMapping("/factures/{id}")
    @Operation(summary = "Supprimer une facture",
            description = "Supprime définitivement une facture du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Facture supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFacture(
            @Parameter(description = "ID de la facture à supprimer") @PathVariable Long id) {
        log.info("Demande de suppression de la facture avec l'ID: {}", id);

        facturationService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== GESTION DES PAIEMENTS ======================

    @PutMapping("/factures/{id}/statut-paiement")
    @Operation(summary = "Mettre à jour le statut de paiement",
            description = "Modifie le statut de paiement d'une facture (EN_ATTENTE, PAYEE, ANNULEE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statut de paiement mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = FactureDTO.class))),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée"),
            @ApiResponse(responseCode = "400", description = "Statut invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<FactureDTO> updateStatutPaiement(
            @Parameter(description = "ID de la facture") @PathVariable Long id,
            @Parameter(description = "Nouveau statut de paiement (EN_ATTENTE, PAYEE, ANNULEE)")
            @RequestParam StatutPaiement statutPaiement) {
        log.info("Demande de mise à jour du statut de paiement de la facture ID: {} vers: {}", id, statutPaiement);

        FactureDTO factureDTO = facturationService.updateStatutPaiement(id, statutPaiement);
        return ResponseEntity.ok(factureDTO);
    }

    @PutMapping("/factures/{id}/methode-paiement")
    @Operation(summary = "Mettre à jour la méthode de paiement",
            description = "Modifie la méthode de paiement d'une facture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Méthode de paiement mise à jour avec succès",
                    content = @Content(schema = @Schema(implementation = FactureDTO.class))),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<FactureDTO> updateMethodePaiement(
            @Parameter(description = "ID de la facture") @PathVariable Long id,
            @Parameter(description = "Nouvelle méthode de paiement (ESPECES, CARTE_BANCAIRE, CHEQUE, VIREMENT)")
            @RequestParam String methodePaiement) {
        log.info("Demande de mise à jour de la méthode de paiement de la facture ID: {} vers: {}", id, methodePaiement);

        FactureDTO factureDTO = facturationService.updateMethodePaiement(id, methodePaiement);
        return ResponseEntity.ok(factureDTO);
    }

    @PostMapping("/factures/{id}/enregistrer-paiement")
    @Operation(summary = "Enregistrer un paiement",
            description = "Enregistre le paiement complet d'une facture avec validation du montant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement enregistré avec succès",
                    content = @Content(schema = @Schema(implementation = FactureDTO.class))),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée"),
            @ApiResponse(responseCode = "400", description = "Montant incorrect ou facture déjà payée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<FactureDTO> enregistrerPaiement(
            @Parameter(description = "ID de la facture") @PathVariable Long id,
            @Parameter(description = "Montant payé (doit correspondre au montant total)")
            @RequestParam BigDecimal montant,
            @Parameter(description = "Méthode de paiement utilisée")
            @RequestParam String methodePaiement) {
        log.info("Demande d'enregistrement de paiement pour la facture ID: {} - Montant: {} - Méthode: {}",
                id, montant, methodePaiement);

        FactureDTO factureDTO = facturationService.enregistrerPaiement(id, montant, methodePaiement);
        return ResponseEntity.ok(factureDTO);
    }

    // ====================== FILTRES ET RECHERCHES ======================

    @GetMapping("/factures/patient/{patientId}")
    @Operation(summary = "Récupérer les factures d'un patient",
            description = "Récupère toutes les factures associées à un patient spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factures du patient récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<FactureDTO>> getFacturesByPatientId(
            @Parameter(description = "ID du patient") @PathVariable Long patientId) {
        log.info("Demande de récupération des factures pour le patient ID: {}", patientId);

        List<FactureDTO> factures = facturationService.getFacturesByPatientId(patientId);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/factures/cadre-administratif/{cadreAdministratifId}")
    @Operation(summary = "Récupérer les factures d'un cadre administratif",
            description = "Récupère toutes les factures créées par un cadre administratif spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factures du cadre administratif récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Cadre administratif non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<FactureDTO>> getFacturesByCadreAdministratifId(
            @Parameter(description = "ID du cadre administratif") @PathVariable Long cadreAdministratifId) {
        log.info("Demande de récupération des factures pour le cadre administratif ID: {}", cadreAdministratifId);

        List<FactureDTO> factures = facturationService.getFacturesByCadreAdministratifId(cadreAdministratifId);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/factures/statut/{statutPaiement}")
    @Operation(summary = "Récupérer les factures par statut de paiement",
            description = "Récupère toutes les factures ayant un statut de paiement spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factures par statut récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<FactureDTO>> getFacturesByStatutPaiement(
            @Parameter(description = "Statut de paiement (EN_ATTENTE, PAYEE, ANNULEE)")
            @PathVariable StatutPaiement statutPaiement) {
        log.info("Demande de récupération des factures avec le statut: {}", statutPaiement);

        List<FactureDTO> factures = facturationService.getFacturesByStatutPaiement(statutPaiement);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/factures/methode-paiement/{methodePaiement}")
    @Operation(summary = "Récupérer les factures par méthode de paiement",
            description = "Récupère toutes les factures payées avec une méthode de paiement spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factures par méthode de paiement récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<FactureDTO>> getFacturesByMethodePaiement(
            @Parameter(description = "Méthode de paiement") @PathVariable String methodePaiement) {
        log.info("Demande de récupération des factures avec la méthode de paiement: {}", methodePaiement);

        List<FactureDTO> factures = facturationService.getFacturesByMethodePaiement(methodePaiement);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/factures/periode")
    @Operation(summary = "Récupérer les factures par période",
            description = "Récupère toutes les factures créées dans une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factures de la période récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<FactureDTO>> getFacturesByDateRange(
            @Parameter(description = "Date de début (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        log.info("Demande de récupération des factures entre le {} et le {}", dateDebut, dateFin);

        List<FactureDTO> factures = facturationService.getFacturesByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(factures);
    }

    // ====================== STATISTIQUES ET ANALYSES FINANCIÈRES ======================

    @GetMapping("/stats/count-by-statut/{statutPaiement}")
    @Operation(summary = "Compter les factures par statut",
            description = "Compte le nombre de factures ayant un statut de paiement spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de factures récupéré avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countFacturesByStatutPaiement(
            @Parameter(description = "Statut de paiement à analyser") @PathVariable StatutPaiement statutPaiement) {
        log.info("Demande de comptage des factures pour le statut: {}", statutPaiement);

        int count = facturationService.countFacturesByStatutPaiement(statutPaiement);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/revenue-total")
    @Operation(summary = "Calculer le revenu total",
            description = "Calcule le revenu total de toutes les factures payées")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revenu total calculé avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<BigDecimal> calculateTotalRevenue() {
        log.info("Demande de calcul du revenu total");

        BigDecimal totalRevenue = facturationService.calculateTotalRevenue();
        return ResponseEntity.ok(totalRevenue);
    }

    @GetMapping("/stats/revenue-periode")
    @Operation(summary = "Calculer le revenu par période",
            description = "Calcule le revenu total des factures payées dans une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revenu de la période calculé avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<BigDecimal> calculateRevenueByDateRange(
            @Parameter(description = "Date de début (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        log.info("Demande de calcul du revenu entre le {} et le {}", dateDebut, dateFin);

        BigDecimal revenue = facturationService.calculateRevenueByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/stats/revenue-by-methode-paiement")
    @Operation(summary = "Analyser les revenus par méthode de paiement",
            description = "Retourne la répartition des revenus par méthode de paiement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analyse des revenus par méthode récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Map<String, BigDecimal>> getRevenueByMethodePaiement() {
        log.info("Demande d'analyse des revenus par méthode de paiement");

        Map<String, BigDecimal> revenueByMethod = facturationService.getRevenueByMethodePaiement();
        return ResponseEntity.ok(revenueByMethod);
    }

    @GetMapping("/stats/count-by-statut")
    @Operation(summary = "Analyser la répartition des factures par statut",
            description = "Retourne le nombre de factures pour chaque statut de paiement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Répartition par statut récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Map<String, Integer>> getFactureCountByStatut() {
        log.info("Demande d'analyse de la répartition des factures par statut");

        Map<String, Integer> countByStatut = facturationService.getFactureCountByStatut();
        return ResponseEntity.ok(countByStatut);
    }
}