package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.StatutCommande;
import com.hospital.HospitalSysteme.service.CommandeService;
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
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Commandes", description = "API pour la gestion des commandes hospitalières, livraisons et suivi des fournisseurs")
public class CommandeController {

    private final CommandeService commandeService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des commandes")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans CommandeController fonctionne");
    }

    // ====================== GESTION CRUD DES COMMANDES ======================

    @PostMapping
    @Operation(summary = "Créer une nouvelle commande",
            description = "Permet de créer une nouvelle commande auprès d'un fournisseur avec validation des dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Commande créée avec succès",
                    content = @Content(schema = @Schema(implementation = CommandeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou dates incohérentes"),
            @ApiResponse(responseCode = "404", description = "Cadre administratif non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<CommandeDTO> createCommande(
            @Parameter(description = "Informations de la commande à créer")
            @Valid @RequestBody CommandeCreationDTO commandeCreationDTO) {
        log.info("Demande de création d'une commande ref: {} chez le fournisseur: {} pour un montant de: {}",
                commandeCreationDTO.getReference(), commandeCreationDTO.getFournisseur(),
                commandeCreationDTO.getMontantTotal());

        CommandeDTO createdCommande = commandeService.createCommande(commandeCreationDTO);
        return new ResponseEntity<>(createdCommande, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une commande par ID",
            description = "Récupère les informations détaillées d'une commande par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande trouvée",
                    content = @Content(schema = @Schema(implementation = CommandeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<CommandeDTO> getCommandeById(
            @Parameter(description = "ID de la commande") @PathVariable Long id) {
        log.info("Demande de récupération de la commande avec l'ID: {}", id);

        CommandeDTO commandeDTO = commandeService.getCommandeById(id);
        return ResponseEntity.ok(commandeDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les commandes",
            description = "Récupère la liste complète de toutes les commandes du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des commandes récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<CommandeDTO>> getAllCommandes() {
        log.info("Demande de récupération de toutes les commandes");

        List<CommandeDTO> commandesList = commandeService.getAllCommandes();
        return ResponseEntity.ok(commandesList);
    }

//    @PutMapping("/{id}")
//    @Operation(summary = "Mettre à jour une commande",
//            description = "Met à jour les informations d'une commande existante (date de livraison, statut)")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Commande mise à jour avec succès",
//                    content = @Content(schema = @Schema(implementation = CommandeDTO.class))),
//            @ApiResponse(responseCode = "404", description = "Commande non trouvée"),
//            @ApiResponse(responseCode = "400", description = "Données invalides")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
//    public ResponseEntity<CommandeDTO> updateCommande(
//            @Parameter(description = "ID de la commande") @PathVariable Long id,
//            @Parameter(description = "Informations à mettre à jour")
//            @Valid @RequestBody CommandeUpdateDTO commandeUpdateDTO) {
//        log.info("Demande de mise à jour de la commande avec l'ID: {}", id);
//
//        CommandeDTO updatedCommande = commandeService.updateCommande(id, commandeUpdateDTO);
//        return ResponseEntity.ok(updatedCommande);
//    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une commande",
            description = "Supprime définitivement une commande du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Commande supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCommande(
            @Parameter(description = "ID de la commande à supprimer") @PathVariable Long id) {
        log.info("Demande de suppression de la commande avec l'ID: {}", id);

        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== GESTION DES STATUTS ET LIVRAISONS ======================

    @PutMapping("/{id}/statut")
    @Operation(summary = "Changer le statut d'une commande",
            description = "Modifie le statut d'une commande existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statut changé avec succès",
                    content = @Content(schema = @Schema(implementation = CommandeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée"),
            @ApiResponse(responseCode = "400", description = "Statut invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<CommandeDTO> changerStatutCommande(
            @Parameter(description = "ID de la commande") @PathVariable Long id,
            @Parameter(description = "Nouveau statut (EN_ATTENTE, CONFIRMEE, EXPEDIEE, LIVREE, ANNULEE)")
            @RequestParam StatutCommande nouveauStatut) {
        log.info("Demande de changement de statut de la commande ID: {} vers: {}", id, nouveauStatut);

        CommandeDTO commandeDTO = commandeService.changerStatutCommande(id, nouveauStatut);
        return ResponseEntity.ok(commandeDTO);
    }

    @PostMapping("/{id}/enregistrer-livraison")
    @Operation(summary = "Enregistrer la livraison d'une commande",
            description = "Enregistre la date de livraison et marque la commande comme livrée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livraison enregistrée avec succès",
                    content = @Content(schema = @Schema(implementation = CommandeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<CommandeDTO> enregistrerLivraison(@PathVariable Long id) {
        // ✅ JUSTE CLIQUER = DATE ACTUELLE
        CommandeDTO commandeDTO = commandeService.enregistrerLivraison(id);
        return ResponseEntity.ok(commandeDTO);
    }
//    public ResponseEntity<CommandeDTO> enregistrerLivraison(
//            @Parameter(description = "ID de la commande") @PathVariable Long id,
//            @Parameter(description = "Date et heure de livraison (format: yyyy-MM-dd'T'HH:mm:ss)")
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateLivraison) {
//        log.info("Demande d'enregistrement de livraison pour la commande ID: {} à la date: {}", id, dateLivraison);
//
//        CommandeDTO commandeDTO = commandeService.enregistrerLivraison(id, dateLivraison);
//        return ResponseEntity.ok(commandeDTO);
//    }

    @PutMapping("/{id}/annuler")
    @Operation(summary = "Annuler une commande",
            description = "Marque une commande comme annulée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande annulée avec succès",
                    content = @Content(schema = @Schema(implementation = CommandeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<CommandeDTO> annulerCommande(
            @Parameter(description = "ID de la commande à annuler") @PathVariable Long id) {
        log.info("Demande d'annulation de la commande avec l'ID: {}", id);

        CommandeDTO commandeDTO = commandeService.annulerCommande(id);
        return ResponseEntity.ok(commandeDTO);
    }

    @PutMapping("/{id}/confirmer")
    @Operation(summary = "Confirmer une commande",
            description = "Marque une commande comme confirmée par le fournisseur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande confirmée avec succès",
                    content = @Content(schema = @Schema(implementation = CommandeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<CommandeDTO> confirmerCommande(
            @Parameter(description = "ID de la commande à confirmer") @PathVariable Long id) {
        log.info("Demande de confirmation de la commande avec l'ID: {}", id);

        CommandeDTO commandeDTO = commandeService.changerStatutCommande(id, StatutCommande.VALIDEE);  // VALIDEE au lieu de CONFIRMEE
        return ResponseEntity.ok(commandeDTO);
    }

    // ====================== FILTRES ET RECHERCHES ======================

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les commandes par statut",
            description = "Récupère toutes les commandes ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commandes par statut récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<CommandeDTO>> getCommandesByStatut(
            @Parameter(description = "Statut des commandes (EN_ATTENTE, CONFIRMEE, EXPEDIEE, LIVREE, ANNULEE)")
            @PathVariable StatutCommande statut) {
        log.info("Demande de récupération des commandes avec le statut: {}", statut);

        List<CommandeDTO> commandes = commandeService.getCommandesByStatut(statut);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/cadre-administratif/{cadreAdministratifId}")
    @Operation(summary = "Récupérer les commandes d'un cadre administratif",
            description = "Récupère toutes les commandes créées par un cadre administratif spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commandes du cadre administratif récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Cadre administratif non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<CommandeDTO>> getCommandesByCadreAdministratif(
            @Parameter(description = "ID du cadre administratif") @PathVariable Long cadreAdministratifId) {
        log.info("Demande de récupération des commandes pour le cadre administratif ID: {}", cadreAdministratifId);

        List<CommandeDTO> commandes = commandeService.getCommandesByCadreAdministratif(cadreAdministratifId);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/fournisseur/{fournisseur}")
    @Operation(summary = "Récupérer les commandes par fournisseur",
            description = "Récupère toutes les commandes d'un fournisseur spécifique (recherche insensible à la casse)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commandes du fournisseur récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<CommandeDTO>> getCommandesByFournisseur(
            @Parameter(description = "Nom du fournisseur (recherche partielle)") @PathVariable String fournisseur) {
        log.info("Demande de récupération des commandes pour le fournisseur: {}", fournisseur);

        List<CommandeDTO> commandes = commandeService.getCommandesByFournisseur(fournisseur);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/periode")
    @Operation(summary = "Récupérer les commandes par période",
            description = "Récupère toutes les commandes passées dans une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commandes de la période récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<CommandeDTO>> getCommandesByDateRange(
            @Parameter(description = "Date de début (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        log.info("Demande de récupération des commandes entre le {} et le {}", dateDebut, dateFin);

        List<CommandeDTO> commandes = commandeService.getCommandesByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(commandes);
    }

    // ====================== STATISTIQUES ET ANALYSES ======================

    @GetMapping("/stats/count-by-statut/{statut}")
    @Operation(summary = "Compter les commandes par statut",
            description = "Compte le nombre de commandes ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de commandes récupéré avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countCommandesByStatut(
            @Parameter(description = "Statut à analyser") @PathVariable StatutCommande statut) {
        log.info("Demande de comptage des commandes pour le statut: {}", statut);

        int count = commandeService.countCommandesByStatut(statut);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/count-by-all-statuts")
    @Operation(summary = "Compter les commandes par tous les statuts",
            description = "Retourne un résumé du nombre de commandes pour chaque statut")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques par statut récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Map<StatutCommande, Integer>> countCommandesByAllStatuts() {
        log.info("Demande de statistiques des commandes par tous les statuts");

        Map<StatutCommande, Integer> stats = commandeService.countCommandesByAllStatuts();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/montant-total-by-statut/{statut}")
    @Operation(summary = "Calculer le montant total par statut",
            description = "Calcule la somme totale des montants pour un statut de commande spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant total calculé avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<BigDecimal> calculateTotalMontantByStatut(
            @Parameter(description = "Statut pour le calcul") @PathVariable StatutCommande statut) {
        log.info("Demande de calcul du montant total pour le statut: {}", statut);

        BigDecimal montantTotal = commandeService.calculateTotalMontantByStatut(statut);
        return ResponseEntity.ok(montantTotal);
    }

    @GetMapping("/stats/montant-total-by-fournisseur/{fournisseur}")
    @Operation(summary = "Calculer le montant total par fournisseur",
            description = "Calcule la somme totale des commandes pour un fournisseur spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant total du fournisseur calculé avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<BigDecimal> calculateTotalMontantByFournisseur(
            @Parameter(description = "Nom du fournisseur") @PathVariable String fournisseur) {
        log.info("Demande de calcul du montant total pour le fournisseur: {}", fournisseur);

        BigDecimal montantTotal = commandeService.calculateTotalMontantByFournisseur(fournisseur);
        return ResponseEntity.ok(montantTotal);
    }

    @GetMapping("/stats/montant-total-by-periode")
    @Operation(summary = "Calculer le montant total par période",
            description = "Calcule la somme totale des commandes dans une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant total de la période calculé avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<BigDecimal> calculateTotalMontantByDateRange(
            @Parameter(description = "Date de début (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        log.info("Demande de calcul du montant total entre le {} et le {}", dateDebut, dateFin);

        BigDecimal montantTotal = commandeService.calculateTotalMontantByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(montantTotal);
    }

    @GetMapping("/stats/montant-by-all-fournisseurs")
    @Operation(summary = "Analyser les montants par tous les fournisseurs",
            description = "Retourne la répartition des montants totaux par fournisseur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analyse des montants par fournisseur récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Map<String, BigDecimal>> calculateTotalMontantByAllFournisseurs() {
        log.info("Demande d'analyse des montants par tous les fournisseurs");

        Map<String, BigDecimal> montantByFournisseur = commandeService.calculateTotalMontantByAllFournisseurs();
        return ResponseEntity.ok(montantByFournisseur);
    }

    @GetMapping("/stats/top-fournisseurs")
    @Operation(summary = "Récupérer les principaux fournisseurs par montant",
            description = "Récupère les fournisseurs les plus importants en termes de montant de commandes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top fournisseurs récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<CommandeDTO>> getTopFournisseursByMontant(
            @Parameter(description = "Nombre de fournisseurs à récupérer (par défaut: 10)")
            @RequestParam(defaultValue = "10") int limit) {
        log.info("Demande de récupération des {} principaux fournisseurs", limit);

        List<CommandeDTO> topFournisseurs = commandeService.getTopFournisseursByMontant(limit);
        return ResponseEntity.ok(topFournisseurs);
    }
}