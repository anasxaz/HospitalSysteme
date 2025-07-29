package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.ServiceHospitalierService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/services-hospitaliers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Services Hospitaliers", description = "API pour la gestion des services hospitaliers, tarifs et catégories")
public class ServiceHospitalierController {

    private final ServiceHospitalierService serviceHospitalierService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des services hospitaliers")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans ServiceHospitalierController fonctionne");
    }

    // ====================== GESTION CRUD DES SERVICES ======================

    @PostMapping
    @Operation(summary = "Créer un nouveau service hospitalier",
            description = "Permet de créer un nouveau service hospitalier avec ses tarifs et catégorie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service hospitalier créé avec succès",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<ServiceDTO> createService(
            @Parameter(description = "Informations du service hospitalier à créer")
            @Valid @RequestBody ServiceCreationDTO serviceCreationDTO) {
        log.info("Demande de création d'un service hospitalier: {}", serviceCreationDTO.getNom());

        ServiceDTO createdService = serviceHospitalierService.createService(serviceCreationDTO);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un service hospitalier par ID",
            description = "Récupère les informations détaillées d'un service hospitalier par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service hospitalier trouvé",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Service hospitalier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<ServiceDTO> getServiceById(
            @Parameter(description = "ID du service hospitalier") @PathVariable Long id) {
        log.info("Demande de récupération du service hospitalier avec l'ID: {}", id);

        ServiceDTO serviceDTO = serviceHospitalierService.getServiceById(id);
        return ResponseEntity.ok(serviceDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les services hospitaliers",
            description = "Récupère la liste complète de tous les services hospitaliers du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des services hospitaliers récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        log.info("Demande de récupération de tous les services hospitaliers");

        List<ServiceDTO> servicesList = serviceHospitalierService.getAllServices();
        return ResponseEntity.ok(servicesList);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un service hospitalier",
            description = "Met à jour les informations d'un service hospitalier existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service hospitalier mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Service hospitalier non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<ServiceDTO> updateService(
            @Parameter(description = "ID du service hospitalier") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour")
            @Valid @RequestBody ServiceUpdateDTO serviceUpdateDTO) {
        log.info("Demande de mise à jour du service hospitalier avec l'ID: {}", id);

        ServiceDTO updatedService = serviceHospitalierService.updateService(id, serviceUpdateDTO);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un service hospitalier",
            description = "Supprime définitivement un service hospitalier du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service hospitalier supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Service hospitalier non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteService(
            @Parameter(description = "ID du service hospitalier à supprimer") @PathVariable Long id) {
        log.info("Demande de suppression du service hospitalier avec l'ID: {}", id);

        serviceHospitalierService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== GESTION DES FILTRES ET RECHERCHES ======================

//    @GetMapping("/categorie/{categorie}")
//    @Operation(summary = "Récupérer les services par catégorie",
//            description = "Récupère tous les services hospitaliers d'une catégorie spécifique")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Services de la catégorie récupérés avec succès")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
//    public ResponseEntity<List<ServiceDTO>> getServicesByCategorie(
//            @Parameter(description = "Catégorie des services recherchés") @PathVariable String categorie) {
//        log.info("Demande de récupération des services de la catégorie: {}", categorie);
//
//        List<ServiceDTO> services = serviceHospitalierService.getServicesByCategorie(categorie);
//        return ResponseEntity.ok(services);
//    }

    @GetMapping("/actifs")
    @Operation(summary = "Récupérer les services hospitaliers actifs",
            description = "Récupère tous les services hospitaliers actuellement actifs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services actifs récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<ServiceDTO>> getServicesActifs() {
        log.info("Demande de récupération des services hospitaliers actifs");

        List<ServiceDTO> services = serviceHospitalierService.getServicesActifs();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/inactifs")
    @Operation(summary = "Récupérer les services hospitaliers inactifs",
            description = "Récupère tous les services hospitaliers actuellement inactifs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services inactifs récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<ServiceDTO>> getServicesInactifs() {
        log.info("Demande de récupération des services hospitaliers inactifs");

        List<ServiceDTO> services = serviceHospitalierService.getServicesInactifs();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des services par nom",
            description = "Effectue une recherche de services hospitaliers par nom (recherche insensible à la casse)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<ServiceDTO>> searchServicesByNom(
            @Parameter(description = "Terme de recherche pour le nom du service")
            @RequestParam String nom) {
        log.info("Demande de recherche de services avec le terme: {}", nom);

        List<ServiceDTO> services = serviceHospitalierService.searchServicesByNom(nom);
        return ResponseEntity.ok(services);
    }

    // ====================== GESTION DE L'ACTIVATION/DÉSACTIVATION ======================

    @PutMapping("/{id}/activer")
    @Operation(summary = "Activer un service hospitalier",
            description = "Active un service hospitalier pour le rendre disponible")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service activé avec succès",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Service hospitalier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<ServiceDTO> activerService(
            @Parameter(description = "ID du service hospitalier à activer") @PathVariable Long id) {
        log.info("Demande d'activation du service hospitalier avec l'ID: {}", id);

        ServiceDTO serviceDTO = serviceHospitalierService.activerService(id);
        return ResponseEntity.ok(serviceDTO);
    }

    @PutMapping("/{id}/desactiver")
    @Operation(summary = "Désactiver un service hospitalier",
            description = "Désactive un service hospitalier pour le rendre indisponible")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service désactivé avec succès",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Service hospitalier non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<ServiceDTO> desactiverService(
            @Parameter(description = "ID du service hospitalier à désactiver") @PathVariable Long id) {
        log.info("Demande de désactivation du service hospitalier avec l'ID: {}", id);

        ServiceDTO serviceDTO = serviceHospitalierService.desactiverService(id);
        return ResponseEntity.ok(serviceDTO);
    }

    // ====================== GESTION DES TARIFS ======================

    @PutMapping("/{id}/tarif")
    @Operation(summary = "Mettre à jour le tarif d'un service",
            description = "Met à jour le tarif d'un service hospitalier existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarif mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Service hospitalier non trouvé"),
            @ApiResponse(responseCode = "400", description = "Tarif invalide (doit être supérieur à 0)")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<ServiceDTO> updateTarif(
            @Parameter(description = "ID du service hospitalier") @PathVariable Long id,
            @Parameter(description = "Nouveau tarif (doit être supérieur à 0)")
            @RequestParam BigDecimal nouveauTarif) {
        log.info("Demande de mise à jour du tarif du service ID: {} vers: {}", id, nouveauTarif);

        ServiceDTO serviceDTO = serviceHospitalierService.updateTarif(id, nouveauTarif);
        return ResponseEntity.ok(serviceDTO);
    }

    @GetMapping("/top-tarifs")
    @Operation(summary = "Récupérer les services les plus chers",
            description = "Récupère les services hospitaliers les plus chers par ordre décroissant de tarif")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services les plus chers récupérés avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<ServiceDTO>> getTopServicesByTarif(
            @Parameter(description = "Nombre de services à récupérer (par défaut: 10)")
            @RequestParam(defaultValue = "10") int limit) {
        log.info("Demande de récupération des {} services les plus chers", limit);

        List<ServiceDTO> services = serviceHospitalierService.getTopServicesByTarif(limit);
        return ResponseEntity.ok(services);
    }

    // ====================== STATISTIQUES ET ANALYSES ======================

//    @GetMapping("/stats/count-by-categorie/{categorie}")
//    @Operation(summary = "Compter les services par catégorie",
//            description = "Compte le nombre de services hospitaliers dans une catégorie spécifique")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Nombre de services récupéré avec succès")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
//    public ResponseEntity<Integer> countServicesByCategorie(
//            @Parameter(description = "Catégorie à analyser") @PathVariable String categorie) {
//        log.info("Demande de comptage des services pour la catégorie: {}", categorie);
//
//        int count = serviceHospitalierService.countServicesByCategorie(categorie);
//        return ResponseEntity.ok(count);
//    }
//
//    @GetMapping("/stats/count-by-all-categories")
//    @Operation(summary = "Compter les services par toutes les catégories",
//            description = "Retourne un résumé du nombre de services par catégorie")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Statistiques par catégorie récupérées avec succès")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
//    public ResponseEntity<Map<String, Integer>> countServicesByAllCategories() {
//        log.info("Demande de statistiques des services par toutes les catégories");
//
//        Map<String, Integer> stats = serviceHospitalierService.countServicesByAllCategories();
//        return ResponseEntity.ok(stats);
//    }
//
//    @GetMapping("/stats/total-tarifs-by-categorie/{categorie}")
//    @Operation(summary = "Calculer le total des tarifs par catégorie",
//            description = "Calcule la somme totale des tarifs pour une catégorie de services")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Total des tarifs calculé avec succès")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
//    public ResponseEntity<BigDecimal> calculateTotalTarifsByCategorie(
//            @Parameter(description = "Catégorie pour le calcul") @PathVariable String categorie) {
//        log.info("Demande de calcul du total des tarifs pour la catégorie: {}", categorie);
//
//        BigDecimal total = serviceHospitalierService.calculateTotalTarifsByCategorie(categorie);
//        return ResponseEntity.ok(total);
//    }

    // Ajoute ces méthodes à ton ServiceHospitalierController

    // ====================== GESTION DES CATÉGORIES RÉELLES ======================
//    @GetMapping("/categories")
//    @Operation(summary = "Récupérer les catégories existantes",
//            description = "Récupère la liste des catégories qui existent réellement dans la base de données")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Liste des catégories existantes récupérée avec succès")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
//    public ResponseEntity<List<String>> getExistingCategories() {
//        log.info("Demande de récupération des catégories existantes en base");
//        List<String> categories = serviceHospitalierService.getAllCategories();
//        return ResponseEntity.ok(categories);
//    }
//
//// Garder l'endpoint avec PathVariable pour la compatibilité (pour les appels directs)
//// Mais ajouter aussi la version avec RequestParam pour Swagger UI dropdown
//
//    @GetMapping("/categorie")
//    @Operation(summary = "Récupérer les services par catégorie",
//            description = "Récupère tous les services hospitaliers d'une catégorie spécifique. Appelez d'abord GET /categories pour voir les options disponibles")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Services de la catégorie récupérés avec succès"),
//            @ApiResponse(responseCode = "400", description = "Catégorie invalide")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
//    public ResponseEntity<List<ServiceDTO>> getServicesByCategorieParam(
//            @Parameter(description = "Catégorie des services (utilisez GET /categories pour voir les options disponibles)",
//                    required = true,
//                    example = "Chirurgie")
//            @RequestParam String categorie) {
//        log.info("Demande de récupération des services de la catégorie: {}", categorie);
//        List<ServiceDTO> services = serviceHospitalierService.getServicesByCategorie(categorie);
//        return ResponseEntity.ok(services);
//    }
//
//    @GetMapping("/stats/count-by-categorie")
//    @Operation(summary = "Compter les services par catégorie",
//            description = "Compte le nombre de services hospitaliers dans une catégorie spécifique. Appelez d'abord GET /categories pour voir les options disponibles")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Nombre de services récupéré avec succès"),
//            @ApiResponse(responseCode = "400", description = "Catégorie invalide")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
//    public ResponseEntity<Integer> countServicesByCategorieParam(
//            @Parameter(description = "Catégorie à analyser (utilisez GET /categories pour voir les options disponibles)",
//                    required = true,
//                    example = "Radiologie")
//            @RequestParam String categorie) {
//        log.info("Demande de comptage des services pour la catégorie: {}", categorie);
//        int count = serviceHospitalierService.countServicesByCategorie(categorie);
//        return ResponseEntity.ok(count);
//    }
//
//    @GetMapping("/stats/total-tarifs-by-categorie")
//    @Operation(summary = "Calculer le total des tarifs par catégorie",
//            description = "Calcule la somme totale des tarifs pour une catégorie de services. Appelez d'abord GET /categories pour voir les options disponibles")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Total des tarifs calculé avec succès"),
//            @ApiResponse(responseCode = "400", description = "Catégorie invalide")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
//    public ResponseEntity<BigDecimal> calculateTotalTarifsByCategorieParam(
//            @Parameter(description = "Catégorie pour le calcul (utilisez GET /categories pour voir les options disponibles)",
//                    required = true,
//                    example = "Diagnostic")
//            @RequestParam String categorie) {
//        log.info("Demande de calcul du total des tarifs pour la catégorie: {}", categorie);
//        BigDecimal total = serviceHospitalierService.calculateTotalTarifsByCategorie(categorie);
//        return ResponseEntity.ok(total);
//    }

// Ajoute ces méthodes à ton ServiceHospitalierController

// ====================== GESTION DES CATÉGORIES AVEC DROPDOWN ======================
@GetMapping("/categories")
@Operation(summary = "Récupérer les catégories existantes",
        description = "Récupère la liste des catégories qui existent réellement dans la base de données")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des catégories existantes récupérée avec succès")
})
@PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
public ResponseEntity<List<String>> getExistingCategories() {
    log.info("Demande de récupération des catégories existantes en base");
    List<String> categories = serviceHospitalierService.getAllCategories();
    return ResponseEntity.ok(categories);
}

    @GetMapping("/categorie")
    @Operation(summary = "Récupérer les services par catégorie",
            description = "Récupère tous les services hospitaliers d'une catégorie spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services de la catégorie récupérés avec succès"),
            @ApiResponse(responseCode = "400", description = "Catégorie invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<List<ServiceDTO>> getServicesByCategorieParam(
            @Parameter(description = "Sélectionnez une catégorie",
                    required = true,
                    schema = @Schema(type = "string",
                            allowableValues = {"Chirurgie", "Radiologie", "Diagnostic", "Urgence", "Cardiologie", "Pédiatrie", "Gynécologie", "Neurologie", "Orthopédie", "Dermatologie", "Psychiatrie", "Ophtalmologie", "ORL", "Laboratoire", "Pharmacie", "Imagerie", "Consultation", "Hospitalisation", "Ambulatoire", "Réanimation"}))
            @RequestParam String categorie) {
        log.info("Demande de récupération des services de la catégorie: {}", categorie);
        List<ServiceDTO> services = serviceHospitalierService.getServicesByCategorie(categorie);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/stats/count-by-categorie")
    @Operation(summary = "Compter les services par catégorie",
            description = "Compte le nombre de services hospitaliers dans une catégorie spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de services récupéré avec succès"),
            @ApiResponse(responseCode = "400", description = "Catégorie invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Integer> countServicesByCategorieParam(
            @Parameter(description = "Sélectionnez une catégorie",
                    required = true,
                    schema = @Schema(type = "string",
                            allowableValues = {"Chirurgie", "Radiologie", "Diagnostic", "Urgence", "Cardiologie", "Pédiatrie", "Gynécologie", "Neurologie", "Orthopédie", "Dermatologie", "Psychiatrie", "Ophtalmologie", "ORL", "Laboratoire", "Pharmacie", "Imagerie", "Consultation", "Hospitalisation", "Ambulatoire", "Réanimation"}))
            @RequestParam String categorie) {
        log.info("Demande de comptage des services pour la catégorie: {}", categorie);
        int count = serviceHospitalierService.countServicesByCategorie(categorie);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/total-tarifs-by-categorie")
    @Operation(summary = "Calculer le total des tarifs par catégorie",
            description = "Calcule la somme totale des tarifs pour une catégorie de services")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total des tarifs calculé avec succès"),
            @ApiResponse(responseCode = "400", description = "Catégorie invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<BigDecimal> calculateTotalTarifsByCategorieParam(
            @Parameter(description = "Sélectionnez une catégorie",
                    required = true,
                    schema = @Schema(type = "string",
                            allowableValues = {"Chirurgie", "Radiologie", "Diagnostic", "Urgence", "Cardiologie", "Pédiatrie", "Gynécologie", "Neurologie", "Orthopédie", "Dermatologie", "Psychiatrie", "Ophtalmologie", "ORL", "Laboratoire", "Pharmacie", "Imagerie", "Consultation", "Hospitalisation", "Ambulatoire", "Réanimation"}))
            @RequestParam String categorie) {
        log.info("Demande de calcul du total des tarifs pour la catégorie: {}", categorie);
        BigDecimal total = serviceHospitalierService.calculateTotalTarifsByCategorie(categorie);
        return ResponseEntity.ok(total);
    }
}