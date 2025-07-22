package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.dto.RendezVousDTO;
import com.hospital.HospitalSysteme.entity.RendezVous;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {


    List<RendezVous> findByPatientId(Long patientId);
    List<RendezVous> findByMedecinId(Long medecinId);
    List<RendezVous> findByStatut(StatutRendezVous statut);

    @Query("SELECT r FROM RendezVous r WHERE r.dateHeure BETWEEN :debut AND :fin")
    List<RendezVous> findByDateHeureBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId AND r.dateHeure BETWEEN :debut AND :fin")
    List<RendezVous> findByMedecinAndDateHeureBetween(Long medecinId, LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT COUNT(r) FROM RendezVous r WHERE r.medecin.id = :medecinId AND r.dateHeure BETWEEN :debut AND :fin")
    Long countByMedecinAndDateHeureBetween(Long medecinId, LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = :patientId AND r.statut = :statut ORDER BY r.dateHeure DESC")
    List<RendezVous> findByPatientIdAndStatutOrderByDateHeureDesc(Long patientId, StatutRendezVous statut);


    // Pour le patient
    List<RendezVous> findByPatientIdAndDateHeureAfter(Long patientId, LocalDateTime now);
    List<RendezVous> findByPatientIdAndDateHeureBefore(Long patientId, LocalDateTime now);
    List<RendezVous> findByPatientIdAndMedecinId(Long patientId, Long medecinId);

    // Pour le médecin
    List<RendezVous> findByMedecinIdAndDateHeureAfter(Long medecinId, LocalDateTime now);
    List<RendezVous> findByMedecinIdAndDateHeureBefore(Long medecinId, LocalDateTime now);

//    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId AND FUNCTION('DATE', r.dateHeure) = :date")
//    List<RendezVous> findByMedecinIdAndDate(Long medecinId, LocalDate date);
    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId AND DATE(r.dateHeure) = :date")
    List<RendezVous> findByMedecinIdAndDate(@Param("medecinId") Long medecinId, @Param("date") LocalDate date);
    List<RendezVous> findByMedecinIdAndPatientId(Long medecinId, Long patientId);


    // Pour le cadre administratif
    @Query("SELECT r FROM RendezVous r WHERE DATE(r.dateHeure) = :date")
    List<RendezVous> findByDate(@Param("date") LocalDate date);

    int countByMedecinDepartementId(Long departementId);

    int countByMedecinIdAndStatut(Long medecinId, StatutRendezVous statut);
    int countByPatientIdAndStatut(Long patientId, StatutRendezVous statut);




    Long countByDateHeureBetween(LocalDateTime debut, LocalDateTime fin);

    Long countByDateHeureGreaterThanEqualAndDateHeureLessThan(LocalDateTime debutJour, LocalDateTime finJour);

    @Query("SELECT r.statut, COUNT(r) FROM RendezVous r GROUP BY r.statut")
    List<Object[]> countRendezVousByStatut();

    @Query("SELECT FUNCTION('MONTH', r.dateHeure), COUNT(r) FROM RendezVous r WHERE FUNCTION('YEAR', r.dateHeure) = :annee GROUP BY FUNCTION('MONTH', r.dateHeure)")
    List<Object[]> countRendezVousByMois(@Param("annee") int annee);

    @Query("SELECT FUNCTION('DAYOFWEEK', r.dateHeure), COUNT(r) FROM RendezVous r WHERE r.medecin.id = :medecinId GROUP BY FUNCTION('DAYOFWEEK', r.dateHeure)")
    List<Object[]> countRendezVousByJourSemaineForMedecin(@Param("medecinId") Long medecinId);




    // Méthode pour récupérer les rendez-vous pour l'exportation
    @Query("SELECT r FROM RendezVous r " +
            "JOIN FETCH r.patient p " +
            "JOIN FETCH r.medecin m " +
            "WHERE (:dateDebut IS NULL OR r.dateHeure >= :dateDebut) AND " +
            "(:dateFin IS NULL OR r.dateHeure <= :dateFin) AND " +
            "(:medecinId IS NULL OR m.id = :medecinId) AND " +
            "(:statut IS NULL OR r.statut = :statut)")
    List<RendezVous> findForExport(@Param("dateDebut") LocalDateTime dateDebut,
                                   @Param("dateFin") LocalDateTime dateFin,
                                   @Param("medecinId") Long medecinId,
                                   @Param("statut") String statut);


//    List<RendezVous> findByMedecinIdAndDateHeureBetween(Long medecinId, LocalDateTime debut, LocalDateTime fin);
    public List<RendezVous> findByMedecinIdAndDateHeureBetween(Long medecinId, LocalDateTime dateDebut, LocalDateTime dateFin);
//public List<RendezVous> findByMedecinIdAndDate(@Param("medecinId") Long medecinId, @Param("date") LocalDate date);



}
