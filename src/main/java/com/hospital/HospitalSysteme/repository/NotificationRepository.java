package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Notification;
import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import com.hospital.HospitalSysteme.entity.enums.TypeNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndStatut(Long userId, StatutNotification statut);

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.dateEnvoi BETWEEN :debut AND :fin")
    List<Notification> findByUserIdAndDateEnvoiBetween(Long userId, LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.statut = 'NON_LUE'")
    Long countUnreadByUserId(Long userId);

    @Query("UPDATE Notification n SET n.statut = 'LUE' WHERE n.user.id = :userId")
    @Modifying
    List<Notification> markAllAsReadByUserId(Long userId);

    @Query("UPDATE Notification n SET n.statut = 'ARCHIVEE' WHERE n.user.id = :userId")
    @Modifying
    List<Notification> markAllAsArchiveeByUserId(Long userId);




    List<Notification> findByStatut(StatutNotification statut);

    List<Notification> findByType(TypeNotification type);

    List<Notification> findByUserIdAndType(Long userId, TypeNotification type);

    List<Notification> findByDateEnvoiBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    int countByUserId(Long userId);

    int countByUserIdAndStatut(Long userId, StatutNotification statut);

    @Query("SELECT n.type, COUNT(n) FROM Notification n WHERE n.user.id = ?1 GROUP BY n.type")
    List<Object[]> countByUserIdGroupByType(Long userId);

    List<Notification> findByUserIdAndStatutNot(Long userId, StatutNotification statut);

}
