package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.NotificationCreationDTO;
import com.hospital.HospitalSysteme.dto.NotificationDTO;
import com.hospital.HospitalSysteme.dto.NotificationUpdateDTO;
import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import com.hospital.HospitalSysteme.entity.enums.TypeNotification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface NotificationService {

    // Opérations CRUD de base
    NotificationDTO createNotification(NotificationCreationDTO notificationCreationDTO);
    NotificationDTO getNotificationById(Long notificationId);
    NotificationDTO updateNotificationStatut(Long notificationId, StatutNotification statut);
    void deleteNotification(Long notificationId);

    // Opérations de recherche et filtrage
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getNotificationsByUserId(Long userId);
    List<NotificationDTO> getNotificationsByStatut(StatutNotification statut);
    List<NotificationDTO> getNotificationsByType(TypeNotification type);
    List<NotificationDTO> getNotificationsByUserIdAndStatut(Long userId, StatutNotification statut);
    List<NotificationDTO> getNotificationsByUserIdAndType(Long userId, TypeNotification type);
    List<NotificationDTO> getNotificationsByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Opérations de gestion
    NotificationDTO marquerCommeLue(Long notificationId);
    List<NotificationDTO> marquerToutesCommeLues(Long userId);
    NotificationDTO archiverNotification(Long notificationId);
    List<NotificationDTO> archiverToutesLesNotifications(Long userId);

    // Opérations de notification système
    NotificationDTO envoyerNotificationRendezVous(Long rendezVousId);
    NotificationDTO envoyerNotificationConsultation(Long consultationId);
    NotificationDTO envoyerNotificationPrescription(Long prescriptionId);
    NotificationDTO envoyerNotificationFacture(Long factureId);
    NotificationDTO envoyerNotificationPlanDeSoins(Long planDeSoinsId);
    NotificationDTO envoyerNotificationSysteme(String titre, String message, Long userId);

    // Statistiques
    int countNotificationsByUserId(Long userId);
    int countNotificationsByUserIdAndStatut(Long userId, StatutNotification statut);
    Map<TypeNotification, Integer> countNotificationsByUserIdGroupByType(Long userId);
}