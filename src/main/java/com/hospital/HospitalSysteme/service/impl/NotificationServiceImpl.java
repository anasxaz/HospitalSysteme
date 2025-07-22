package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.NotificationCreationDTO;
import com.hospital.HospitalSysteme.dto.NotificationDTO;
import com.hospital.HospitalSysteme.entity.Notification;
import com.hospital.HospitalSysteme.entity.PlanDeSoins;
import com.hospital.HospitalSysteme.entity.RendezVous;
import com.hospital.HospitalSysteme.entity.User;
import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import com.hospital.HospitalSysteme.entity.enums.TypeNotification;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.NotificationMapper;
import com.hospital.HospitalSysteme.mapper.PlanDeSoinsMapper;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    // Dependency Injection
    private PatientRepository patientRepository;
    private PlanDeSoinsRepository planDeSoinsRepository;
    private InfirmierRepository infirmierRepository;
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private RendezVousRepository rendezVousRepository;
    private ConsultationRepository consultationRepository;
    private PrescriptionRepository prescriptionRepository;
    private FactureRepository factureRepository;


    private PlanDeSoinsMapper planDeSoinsMapper;
    private NotificationMapper notificationMapper;








    @Override
    public NotificationDTO createNotification(NotificationCreationDTO notificationCreationDTO) {
        log.info("Création d'une nouvelle notification intitulé : {}", notificationCreationDTO.getTitre());

        // Convert notificationCreationDTO to notification JPA Entity
        Notification notification = notificationMapper.toEntity(notificationCreationDTO);

        // plan de soins JPA Entity
        Notification savedNotification = notificationRepository.save(notification);

        log.info("Notification créée avec succès avec l'ID : {}", savedNotification.getId());

        // Convert savedPlanDeSoins JPA Entity into DTO object
        return notificationMapper.toDTO(savedNotification);
    }



    @Override
    public NotificationDTO getNotificationById(Long notificationId) {
        log.info("Récupération de la notification avec l'ID : {}", notificationId);

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new ResourceNotFoundException("Notification non trouvée avec l'ID : " + notificationId)
        );

        return notificationMapper.toDTO(notification);
    }

    @Override
    public NotificationDTO updateNotificationStatut(Long notificationId, StatutNotification statut) {
        log.info("Mise à jour du statut de la notification d'ID : {} avec le statut : {}", notificationId, statut);

        // Récupérer la notification existante
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification non trouvée avec l'ID : " + notificationId));

        // Mettre à jour le statut de la notification
        notification.setStatut(statut);

        // Sauvegarder les modifications
        Notification updatedNotification = notificationRepository.save(notification);

        log.info("Notification mis à jour avec succès");

        // Convertir et retourner la notification mis à jour
        return notificationMapper.toDTO(updatedNotification);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        log.info("Suppression de la notification avec l'ID : {}", notificationId);

        if (!notificationRepository.existsById(notificationId)){
            throw new ResourceNotFoundException("Notification non trouvée avec l'ID : " + notificationId);
        }

        notificationRepository.deleteById(notificationId);

        log.info("Notification supprimée avec succès");

    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        log.info("Récupération de toutes les notifications");

        List<Notification> notifications = notificationRepository.findAll();

        return notifications.stream()
                .map((notification) -> notificationMapper.toDTO(notification))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        log.info("Récupération des notifications pour l'utilisateur avec l'ID : {}", userId);

        // Vérifier que le patient existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        List<Notification> notifications = notificationRepository.findByUserId(userId);

        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByStatut(StatutNotification statut) {
        log.info("Récupération de notifications avec le statut : {}", statut);

        List<Notification> notifications = notificationRepository.findByStatut(statut);

        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByType(TypeNotification type) {
        log.info("Récupération de notifications avec le type : {}", type);

        List<Notification> notifications = notificationRepository.findByType(type);

        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserIdAndStatut(Long userId, StatutNotification statut) {
        log.info("Récupération de notifications pour l'utilisateur avec l'ID : {} et de statut : {}", userId, statut);

        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        List<Notification> notifications = notificationRepository.findByUserIdAndStatut(userId, statut);

        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserIdAndType(Long userId, TypeNotification type) {
        log.info("Récupération de notifications pour l'utilisateur avec l'ID : {} et de type de notification : {}", userId, type);

        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        List<Notification> notifications = notificationRepository.findByUserIdAndType(userId, type);

        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        log.info("Récupération de notifications entre le : {} et le : {}", dateDebut, dateFin);

        List<Notification> notifications = notificationRepository.findByDateEnvoiBetween(dateDebut, dateFin);

        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO marquerCommeLue(Long notificationId) {
        log.info("Mise à jour du statut de la notification d'ID : {} qui sera marquer comme lue", notificationId);

        // Récupérer la notification existante
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification non trouvée avec l'ID : " + notificationId));

        // Mettre à jour le statut de la notification
        notification.setStatut(StatutNotification.LUE);

        // Sauvegarder les modifications
        Notification updatedNotification = notificationRepository.save(notification);

        log.info("Statut mis à jour avec succès");

        // Convertir et retourner la notification mis à jour
        return notificationMapper.toDTO(updatedNotification);
    }

    @Override
    public List<NotificationDTO> marquerToutesCommeLues(Long userId) {
        log.info("Mise à jour du statut de toutes les notifications de l'utilisateur avec l'ID : {} qui seront marquer comme lues", userId);

        // Vérifier l'existance de l'utilisateur
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        // Récupérer toutes les notifications non lues de l'utilisateur
        List<Notification> notifications = notificationRepository.findByUserIdAndStatut(userId, StatutNotification.NON_LUE);

        // Mettre à jour le statut de chaque notification
        for (Notification notification : notifications) {
            notification.setStatut(StatutNotification.LUE);
        }

        // Sauvegarder les modifications
        List<Notification> updatedNotifications = notificationRepository.saveAll(notifications);

        log.info("{} notifications marquées comme lues pour l'utilisateur {}", notifications.size(), userId);

        // Convertir et retourner les notifications mises à jour
        return updatedNotifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO archiverNotification(Long notificationId) {
        log.info("Mise à jour du statut de la notification d'ID : {} qui sera marquer comme archivée", notificationId);

        // Récupérer la notification existante
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification non trouvée avec l'ID : " + notificationId));

        // Mettre à jour le statut de la notification
        notification.setStatut(StatutNotification.ARCHIVEE);

        // Sauvegarder les modifications
        Notification updatedNotification = notificationRepository.save(notification);

        log.info("Statut mis comme archive avec succès");

        // Convertir et retourner la notification mis à jour
        return notificationMapper.toDTO(updatedNotification);
    }

    @Override
    public List<NotificationDTO> archiverToutesLesNotifications(Long userId) {
        log.info("Mise à jour du statut de toutes les notifications de l'utilisateur avec l'ID : {} qui seront marquer comme archivées", userId);

        // Vérifier l'existance de l'utilisateur
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        // Récupérer toutes les notifications non archivées de l'utilisateur
        List<Notification> notifications = notificationRepository.findByUserIdAndStatutNot(userId, StatutNotification.ARCHIVEE);

        // Mettre à jour le statut de chaque notification
        for (Notification notification : notifications) {
            notification.setStatut(StatutNotification.ARCHIVEE);
        }

        // Sauvegarder les modifications
        List<Notification> updatedNotifications = notificationRepository.saveAll(notifications);

        log.info("{} notifications archivées pour l'utilisateur {}", notifications.size(), userId);

        // Convertir et retourner les notifications mises à jour
        return updatedNotifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }


    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public NotificationDTO envoyerNotificationRendezVous(Long rendezVousId) {
        log.info("Envoi d'une notification pour le rendez-vous avec l'ID : {}", rendezVousId);

        // Récupérer le rendez-vous
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId));

        // Créer la notification
        Notification notification = new Notification();
        notification.setTitre("Rappel de rendez-vous");
        notification.setContenu("Vous avez un rendez-vous prévu le " +
                rendezVous.getDateHeure().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")) +
                " avec Dr. " + rendezVous.getMedecin().getNom() + " " + rendezVous.getMedecin().getPrenom());
        notification.setType(String.valueOf(TypeNotification.RENDEZ_VOUS));
        notification.setStatut(StatutNotification.NON_LUE);
        notification.setUser(rendezVous.getPatient());

        // Sauvegarder la notification
        Notification savedNotification = notificationRepository.save(notification);

        log.info("Notification de rendez-vous envoyée avec succès avec l'ID : {}", savedNotification.getId());

        return notificationMapper.toDTO(savedNotification);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public NotificationDTO envoyerNotificationConsultation(Long consultationId) {
        return null;
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public NotificationDTO envoyerNotificationPrescription(Long prescriptionId) {
        return null;
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public NotificationDTO envoyerNotificationFacture(Long factureId) {
        return null;
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public NotificationDTO envoyerNotificationPlanDeSoins(Long planDeSoinsId) {
        log.info("Envoi d'une notification pour le plan de soins avec l'ID : {}", planDeSoinsId);

        // Récupérer le plan de soins
        PlanDeSoins planDeSoins = planDeSoinsRepository.findById(planDeSoinsId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan de soins non trouvé avec l'ID : " + planDeSoinsId));

        // Créer la notification
        Notification notification = new Notification();
        notification.setTitre("Nouveau plan de soins");
        notification.setContenu("Un plan de soins a été créé ou mis à jour pour vous. " +
                "Période : du " + planDeSoins.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " au " + planDeSoins.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ". Infirmier(ère) responsable : " + planDeSoins.getInfirmier().getPrenom() + " " +
                planDeSoins.getInfirmier().getNom());
        notification.setType(String.valueOf(TypeNotification.PLAN_DE_SOINS));
        notification.setStatut(StatutNotification.NON_LUE);
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setUser(planDeSoins.getPatient());

        // Sauvegarder la notification
        Notification savedNotification = notificationRepository.save(notification);

        log.info("Notification de plan de soins envoyée avec succès avec l'ID : {}", savedNotification.getId());

        return notificationMapper.toDTO(savedNotification);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public NotificationDTO envoyerNotificationSysteme(String titre, String message, Long userId) {
        log.info("Envoi d'une notification système à l'utilisateur avec l'ID : {}", userId);

        // Vérifier que l'utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));

        // Créer la notification
        Notification notification = new Notification();
        notification.setTitre(titre);
        notification.setContenu(message);
        notification.setType(String.valueOf(TypeNotification.SYSTEME));
        notification.setStatut(StatutNotification.NON_LUE);
        notification.setUser(user);

        // Sauvegarder la notification
        Notification savedNotification = notificationRepository.save(notification);

        log.info("Notification système envoyée avec succès avec l'ID : {}", savedNotification.getId());

        return notificationMapper.toDTO(savedNotification);
    }

    @Override
    public int countNotificationsByUserId(Long userId) {
        log.info("Comptage des notifications pour l'utilisateur avec l'ID : {}", userId);

        // Vérifier que l'utilisateur existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        return notificationRepository.countByUserId(userId);
    }

    @Override
    public int countNotificationsByUserIdAndStatut(Long userId, StatutNotification statut) {
        log.info("Comptage des notifications pour l'utilisateur avec l'ID : {} avec le statut de la notification : {}", userId, statut);

        // Vérifier que l'utilisateur existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        return notificationRepository.countByUserIdAndStatut(userId, statut);
    }


    // Je ne sais pas
    // Je vais le compléter par Claude pour s'inspirer
    @Override
    public Map<TypeNotification, Integer> countNotificationsByUserIdGroupByType(Long userId) {
        log.info("Comptage des notifications groupé par type pour l'utilisateur avec l'ID : {}", userId);

        // Vérifier que l'utilisateur existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        List<Object[]> results = notificationRepository.countByUserIdGroupByType(userId);

        Map<TypeNotification, Integer> countByType = new HashMap<>();
        for (Object[] result : results) {
            TypeNotification type = (TypeNotification) result[0];
            Long count = (Long) result[1];
            countByType.put(type, count.intValue());
        }

        return countByType;
    }
}
