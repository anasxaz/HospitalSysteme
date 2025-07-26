package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import com.hospital.HospitalSysteme.entity.enums.TypeNotification;
import com.hospital.HospitalSysteme.service.NotificationService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notifications", description = "API pour la gestion des notifications hospitalières et communications système")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des notifications")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans NotificationController fonctionne");
    }

    // ====================== GESTION CRUD DES NOTIFICATIONS ======================

    @PostMapping
    @Operation(summary = "Créer une nouvelle notification",
            description = "Permet de créer une nouvelle notification pour un utilisateur spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification créée avec succès",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<NotificationDTO> createNotification(
            @Parameter(description = "Informations de la notification à créer")
            @Valid @RequestBody NotificationCreationDTO notificationCreationDTO) {
        log.info("Demande de création d'une notification '{}' pour l'utilisateur ID: {}",
                notificationCreationDTO.getTitre(), notificationCreationDTO.getUserId());

        NotificationDTO createdNotification = notificationService.createNotification(notificationCreationDTO);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une notification par ID",
            description = "Récupère les informations détaillées d'une notification par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification trouvée",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notification non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<NotificationDTO> getNotificationById(
            @Parameter(description = "ID de la notification") @PathVariable Long id) {
        log.info("Demande de récupération de la notification avec l'ID: {}", id);

        NotificationDTO notificationDTO = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notificationDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les notifications",
            description = "Récupère la liste complète de toutes les notifications du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des notifications récupérée avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        log.info("Demande de récupération de toutes les notifications");

        List<NotificationDTO> notificationsList = notificationService.getAllNotifications();
        return ResponseEntity.ok(notificationsList);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une notification",
            description = "Supprime définitivement une notification du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Notification non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID de la notification à supprimer") @PathVariable Long id) {
        log.info("Demande de suppression de la notification avec l'ID: {}", id);

        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    // ====================== GESTION DES STATUTS ======================

    @PutMapping("/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'une notification",
            description = "Modifie le statut d'une notification (NON_LUE, LUE, ARCHIVEE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statut mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notification non trouvée"),
            @ApiResponse(responseCode = "400", description = "Statut invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<NotificationDTO> updateNotificationStatut(
            @Parameter(description = "ID de la notification") @PathVariable Long id,
            @Parameter(description = "Nouveau statut (NON_LUE, LUE, ARCHIVEE)")
            @RequestParam StatutNotification statut) {
        log.info("Demande de mise à jour du statut de la notification ID: {} vers: {}", id, statut);

        NotificationDTO notificationDTO = notificationService.updateNotificationStatut(id, statut);
        return ResponseEntity.ok(notificationDTO);
    }

    @PutMapping("/{id}/marquer-comme-lue")
    @Operation(summary = "Marquer une notification comme lue",
            description = "Marque une notification spécifique comme lue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marquée comme lue",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notification non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<NotificationDTO> marquerCommeLue(
            @Parameter(description = "ID de la notification à marquer comme lue") @PathVariable Long id) {
        log.info("Demande de marquage comme lue de la notification avec l'ID: {}", id);

        NotificationDTO notificationDTO = notificationService.marquerCommeLue(id);
        return ResponseEntity.ok(notificationDTO);
    }

    @PutMapping("/user/{userId}/marquer-toutes-comme-lues")
    @Operation(summary = "Marquer toutes les notifications d'un utilisateur comme lues",
            description = "Marque toutes les notifications non lues d'un utilisateur comme lues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Toutes les notifications marquées comme lues"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<NotificationDTO>> marquerToutesCommeLues(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId) {
        log.info("Demande de marquage de toutes les notifications comme lues pour l'utilisateur ID: {}", userId);

        List<NotificationDTO> notificationsDTO = notificationService.marquerToutesCommeLues(userId);
        return ResponseEntity.ok(notificationsDTO);
    }

    @PutMapping("/{id}/archiver")
    @Operation(summary = "Archiver une notification",
            description = "Archive une notification spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification archivée avec succès",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notification non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<NotificationDTO> archiverNotification(
            @Parameter(description = "ID de la notification à archiver") @PathVariable Long id) {
        log.info("Demande d'archivage de la notification avec l'ID: {}", id);

        NotificationDTO notificationDTO = notificationService.archiverNotification(id);
        return ResponseEntity.ok(notificationDTO);
    }

    @PutMapping("/user/{userId}/archiver-toutes")
    @Operation(summary = "Archiver toutes les notifications d'un utilisateur",
            description = "Archive toutes les notifications non archivées d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Toutes les notifications archivées"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<NotificationDTO>> archiverToutesLesNotifications(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId) {
        log.info("Demande d'archivage de toutes les notifications pour l'utilisateur ID: {}", userId);

        List<NotificationDTO> notificationsDTO = notificationService.archiverToutesLesNotifications(userId);
        return ResponseEntity.ok(notificationsDTO);
    }

    // ====================== FILTRES ET RECHERCHES ======================

    @GetMapping("/user/{userId}")
    @Operation(summary = "Récupérer les notifications d'un utilisateur",
            description = "Récupère toutes les notifications d'un utilisateur spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications de l'utilisateur récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId) {
        log.info("Demande de récupération des notifications pour l'utilisateur ID: {}", userId);

        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les notifications par statut",
            description = "Récupère toutes les notifications ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications par statut récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByStatut(
            @Parameter(description = "Statut des notifications (NON_LUE, LUE, ARCHIVEE)")
            @PathVariable StatutNotification statut) {
        log.info("Demande de récupération des notifications avec le statut: {}", statut);

        List<NotificationDTO> notifications = notificationService.getNotificationsByStatut(statut);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Récupérer les notifications par type",
            description = "Récupère toutes les notifications d'un type spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications par type récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByType(
            @Parameter(description = "Type de notification (RENDEZ_VOUS, PRESCRIPTION, CONSULTATION, etc.)")
            @PathVariable TypeNotification type) {
        log.info("Demande de récupération des notifications de type: {}", type);

        List<NotificationDTO> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/statut/{statut}")
    @Operation(summary = "Récupérer les notifications d'un utilisateur par statut",
            description = "Récupère les notifications d'un utilisateur ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications filtrées récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserIdAndStatut(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId,
            @Parameter(description = "Statut des notifications") @PathVariable StatutNotification statut) {
        log.info("Demande de récupération des notifications pour l'utilisateur ID: {} avec statut: {}", userId, statut);

        List<NotificationDTO> notifications = notificationService.getNotificationsByUserIdAndStatut(userId, statut);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/type/{type}")
    @Operation(summary = "Récupérer les notifications d'un utilisateur par type",
            description = "Récupère les notifications d'un utilisateur d'un type spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications filtrées récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserIdAndType(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId,
            @Parameter(description = "Type de notification") @PathVariable TypeNotification type) {
        log.info("Demande de récupération des notifications pour l'utilisateur ID: {} de type: {}", userId, type);

        List<NotificationDTO> notifications = notificationService.getNotificationsByUserIdAndType(userId, type);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/periode")
    @Operation(summary = "Récupérer les notifications par période",
            description = "Récupère toutes les notifications envoyées dans une période donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications de la période récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByDateRange(
            @Parameter(description = "Date de début (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        log.info("Demande de récupération des notifications entre le {} et le {}", dateDebut, dateFin);

        List<NotificationDTO> notifications = notificationService.getNotificationsByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(notifications);
    }

    // ====================== ENVOIS AUTOMATIQUES DE NOTIFICATIONS ======================

    @PostMapping("/envoyer/rendez-vous/{rendezVousId}")
    @Operation(summary = "Envoyer une notification de rendez-vous",
            description = "Envoie automatiquement une notification de rappel pour un rendez-vous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification de rendez-vous envoyée",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<NotificationDTO> envoyerNotificationRendezVous(
            @Parameter(description = "ID du rendez-vous") @PathVariable Long rendezVousId) {
        log.info("Demande d'envoi de notification pour le rendez-vous ID: {}", rendezVousId);

        NotificationDTO notificationDTO = notificationService.envoyerNotificationRendezVous(rendezVousId);
        return new ResponseEntity<>(notificationDTO, HttpStatus.CREATED);
    }

    @PostMapping("/envoyer/plan-de-soins/{planDeSoinsId}")
    @Operation(summary = "Envoyer une notification de plan de soins",
            description = "Envoie automatiquement une notification pour un plan de soins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification de plan de soins envoyée",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Plan de soins non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<NotificationDTO> envoyerNotificationPlanDeSoins(
            @Parameter(description = "ID du plan de soins") @PathVariable Long planDeSoinsId) {
        log.info("Demande d'envoi de notification pour le plan de soins ID: {}", planDeSoinsId);

        NotificationDTO notificationDTO = notificationService.envoyerNotificationPlanDeSoins(planDeSoinsId);
        return new ResponseEntity<>(notificationDTO, HttpStatus.CREATED);
    }

    @PostMapping("/envoyer/consultation/{consultationId}")
    @Operation(summary = "Envoyer une notification de consultation",
            description = "Envoie automatiquement une notification pour une consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification de consultation envoyée",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<NotificationDTO> envoyerNotificationConsultation(
            @Parameter(description = "ID de la consultation") @PathVariable Long consultationId) {
        log.info("Demande d'envoi de notification pour la consultation ID: {}", consultationId);

        NotificationDTO notificationDTO = notificationService.envoyerNotificationConsultation(consultationId);
        return new ResponseEntity<>(notificationDTO, HttpStatus.CREATED);
    }

    @PostMapping("/envoyer/prescription/{prescriptionId}")
    @Operation(summary = "Envoyer une notification de prescription",
            description = "Envoie automatiquement une notification pour une prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification de prescription envoyée",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Prescription non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
    public ResponseEntity<NotificationDTO> envoyerNotificationPrescription(
            @Parameter(description = "ID de la prescription") @PathVariable Long prescriptionId) {
        log.info("Demande d'envoi de notification pour la prescription ID: {}", prescriptionId);

        NotificationDTO notificationDTO = notificationService.envoyerNotificationPrescription(prescriptionId);
        return new ResponseEntity<>(notificationDTO, HttpStatus.CREATED);
    }

    @PostMapping("/envoyer/facture/{factureId}")
    @Operation(summary = "Envoyer une notification de facture",
            description = "Envoie automatiquement une notification pour une facture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification de facture envoyée",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<NotificationDTO> envoyerNotificationFacture(
            @Parameter(description = "ID de la facture") @PathVariable Long factureId) {
        log.info("Demande d'envoi de notification pour la facture ID: {}", factureId);

        NotificationDTO notificationDTO = notificationService.envoyerNotificationFacture(factureId);
        return new ResponseEntity<>(notificationDTO, HttpStatus.CREATED);
    }

    @PostMapping("/envoyer/systeme")
    @Operation(summary = "Envoyer une notification système",
            description = "Envoie une notification système personnalisée à un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification système envoyée",
                    content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<NotificationDTO> envoyerNotificationSysteme(
            @Parameter(description = "Titre de la notification") @RequestParam String titre,
            @Parameter(description = "Message de la notification") @RequestParam String message,
            @Parameter(description = "ID de l'utilisateur destinataire") @RequestParam Long userId) {
        log.info("Demande d'envoi de notification système '{}' à l'utilisateur ID: {}", titre, userId);

        NotificationDTO notificationDTO = notificationService.envoyerNotificationSysteme(titre, message, userId);
        return new ResponseEntity<>(notificationDTO, HttpStatus.CREATED);
    }

    // ====================== STATISTIQUES ET COMPTEURS ======================

    @GetMapping("/stats/count/user/{userId}")
    @Operation(summary = "Compter les notifications d'un utilisateur",
            description = "Compte le nombre total de notifications pour un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de notifications récupéré"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<Integer> countNotificationsByUserId(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId) {
        log.info("Demande de comptage des notifications pour l'utilisateur ID: {}", userId);

        int count = notificationService.countNotificationsByUserId(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/count/user/{userId}/statut/{statut}")
    @Operation(summary = "Compter les notifications d'un utilisateur par statut",
            description = "Compte le nombre de notifications d'un utilisateur ayant un statut spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de notifications récupéré"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<Integer> countNotificationsByUserIdAndStatut(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId,
            @Parameter(description = "Statut des notifications") @PathVariable StatutNotification statut) {
        log.info("Demande de comptage des notifications pour l'utilisateur ID: {} avec statut: {}", userId, statut);

        int count = notificationService.countNotificationsByUserIdAndStatut(userId, statut);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/count/user/{userId}/group-by-type")
    @Operation(summary = "Analyser les notifications d'un utilisateur par type",
            description = "Retourne la répartition des notifications d'un utilisateur par type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques par type récupérées"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER', 'PATIENT')")
    public ResponseEntity<Map<TypeNotification, Integer>> countNotificationsByUserIdGroupByType(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId) {
        log.info("Demande d'analyse des notifications par type pour l'utilisateur ID: {}", userId);

        Map<TypeNotification, Integer> countByType = notificationService.countNotificationsByUserIdGroupByType(userId);
        return ResponseEntity.ok(countByType);
    }
}