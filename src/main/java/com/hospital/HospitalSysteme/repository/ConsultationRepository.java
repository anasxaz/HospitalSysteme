package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

//    List<Consultation> findByMedecinId(Long medecinId);
    @Query("SELECT c FROM Consultation c WHERE c.medecin.id = :medecinId")
    List<Consultation> findByMedecinId(@Param("medecinId") Long medecinId);
//    List<Consultation> findByMedecinIdAndPatientId(Long medecinId, Long patientId);
    @Query("SELECT c FROM Consultation c WHERE c.medecin.id = :medecinId AND c.dossierMedical.patient.id = :patientId")
    List<Consultation> findByMedecinIdAndPatientId(@Param("medecinId") Long medecinId, @Param("patientId") Long patientId);
//    List<Consultation> findByDossierMedicalId(Long dossierMedicalId);
    @Query("SELECT c FROM Consultation c WHERE c.dossierMedical.id = :dossierMedicalId")
    List<Consultation> findByDossierMedicalId(@Param("dossierMedicalId") Long dossierMedicalId);

    @Query("SELECT c FROM Consultation c WHERE c.date BETWEEN :debut AND :fin")
    List<Consultation> findByDateBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT c FROM Consultation c WHERE c.medecin.id=:medecinId AND c.date BETWEEN :debut AND :fin")
    List<Consultation> findByMedecinIdAndDateBetween(Long medecinId, LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT c FROM Consultation c JOIN c.dossierMedical d JOIN d.patient p WHERE p.id = :patientId ORDER BY c.date DESC")
    List<Consultation> findByPatientIdOrderByDateDesc(Long patientId);

    @Query("SELECT c FROM Consultation c WHERE LOWER(c.diagnostic) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(c.symptomes) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<Consultation> findByDiagnosticOrSymptomesContaining(String terme);

//    List<Consultation> findByDossierMedicalIdOrderByDateConsultationDesc(Long dossierMedicalId);
    List<Consultation> findByDossierMedicalIdOrderByDateDesc(Long dossierMedicalId);
//    int countByMedecinDepartementId(Long departementId);
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.medecin.departement.id = :departementId")
    int countByMedecinDepartementId(@Param("departementId") Long departementId);

//    @Query("SELECT c FROM Consultation c WHERE c.dossierMedical.patient.id = :patientId")
//    List<Consultation> findByPatientId(@Param("patientId") Long patientId);
//    List<Consultation> findByPatientId(Long patientId);
    // Ajoutez une nouvelle méthode
    @Query("SELECT c FROM Consultation c WHERE c.dossierMedical.patient.id = :patientId")
    List<Consultation> findByDossierMedicalPatientId(@Param("patientId") Long patientId);
//    List<Consultation> findByDateConsultation(LocalDate date);
    @Query("SELECT c FROM Consultation c WHERE FUNCTION('DATE', c.date) = :dateConsultation")
    List<Consultation> findByDateConsultation(@Param("dateConsultation") LocalDate dateConsultation);
    int countByMedecinId(Long medecinId);
//    int countByPatientId(Long patientId);
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.dossierMedical.patient.id = :patientId")
    int countByPatientId(@Param("patientId") Long patientId);



    Long countByDateBetween(LocalDateTime debut, LocalDateTime fin);

    Long countByDateGreaterThanEqualAndDateLessThan(LocalDateTime debutJour, LocalDateTime finJour);

    @Query("SELECT FUNCTION('MONTH', c.date), COUNT(c) FROM Consultation c WHERE FUNCTION('YEAR', c.date) = :annee GROUP BY FUNCTION('MONTH', c.date)")
    List<Object[]> countConsultationsByMois(@Param("annee") int annee);

//    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.medecin.id = :medecinId AND c.date BETWEEN :debut AND :fin")
//    Long countByMedecinIdAndDateBetween(@Param("medecinId") Long medecinId, @Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);


    @Query("SELECT COUNT(c) FROM Consultation c " +
            "WHERE c.medecin.departement.id = :departementId " +
            "AND c.date BETWEEN :debut AND :fin")
    Long countByDepartementIdAndDateBetween(@Param("departementId") Long departementId,
                                            @Param("debut") LocalDateTime debut,
                                            @Param("fin") LocalDateTime fin);
//    Long countByDepartementIdAndDateBetween(Long deptId, LocalDateTime debut, LocalDateTime fin);

//    @Query("SELECT COUNT(DISTINCT c.patient) FROM Consultation c " +
//            "WHERE c.medecin.departement.id = :departementId " +
//            "AND c.date BETWEEN :debut AND :fin")
//    Long countDistinctPatientsByDepartementAndDateBetween(@Param("departementId") Long departementId,
//                                                          @Param("debut") LocalDateTime debut,
//                                                          @Param("fin") LocalDateTime fin);
    @Query("SELECT COUNT(DISTINCT c.dossierMedical.patient) FROM Consultation c " +
            "WHERE c.medecin.departement.id = :departementId " +
            "AND c.date BETWEEN :debut AND :fin")
    Long countDistinctPatientsByDepartementAndDateBetween(@Param("departementId") Long departementId,
                                                          @Param("debut") LocalDateTime debut,
                                                          @Param("fin") LocalDateTime fin);
//    Long countDistinctPatientsByDepartementAndDateBetween(Long deptId, LocalDateTime debut, LocalDateTime fin);

//    @Query("SELECT COUNT(DISTINCT c.patient) FROM Consultation c " +
//            "WHERE c.medecin.id = :medecinId " +
//            "AND c.date BETWEEN :debut AND :fin")
//    Long countDistinctPatientsByMedecinAndDateBetween(@Param("medecinId") Long medecinId,
//                                                      @Param("debut") LocalDateTime debut,
//                                                      @Param("fin") LocalDateTime fin);
    @Query("SELECT COUNT(DISTINCT c.dossierMedical.patient) FROM Consultation c " +
            "WHERE c.medecin.id = :medecinId " +
            "AND c.date BETWEEN :debut AND :fin")
    Long countDistinctPatientsByMedecinAndDateBetween(@Param("medecinId") Long medecinId,
                                                      @Param("debut") LocalDateTime debut,
                                                      @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(c) FROM Consultation c " +
            "WHERE c.medecin.id = :medecinId " +
            "AND c.date BETWEEN :debut AND :fin")
    Long countByMedecinIdAndDateBetween(@Param("medecinId") Long medecinId,
                                        @Param("debut") LocalDateTime debut,
                                        @Param("fin") LocalDateTime fin);

    // Si vous avez un champ statut dans Consultation :
//    @Query("SELECT COUNT(c) FROM Consultation c " +
//            "WHERE c.medecin.id = :medecinId " +
//            "AND c.statut = :statut " +
//            "AND c.date BETWEEN :debut AND :fin")
//    Long countByMedecinIdAndStatutAndDateBetween(@Param("medecinId") Long medecinId,
//                                                 @Param("statut") String statut,
//                                                 @Param("debut") LocalDateTime debut,
//                                                 @Param("fin") LocalDateTime fin);

    @Query("SELECT MONTH(c.date), COUNT(c) FROM Consultation c " +
            "WHERE c.date BETWEEN :debut AND :fin " +
            "GROUP BY MONTH(c.date)")
    List<Object[]> countConsultationsByMoisForPeriod(@Param("debut") LocalDateTime debut,
                                                     @Param("fin") LocalDateTime fin);


    // Consultations par mois pour un département spécifique
    @Query("SELECT FUNCTION('MONTH', c.date), COUNT(c) FROM Consultation c " +
            "WHERE c.medecin.departement.id = :deptId AND FUNCTION('YEAR', c.date) = :annee " +
            "GROUP BY FUNCTION('MONTH', c.date)")
    List<Object[]> countConsultationsByMoisAndDepartement(@Param("deptId") Long deptId, @Param("annee") int annee);

    // Consultations par semaine (dernières 4 semaines)
    @Query("SELECT FUNCTION('WEEK', c.date), COUNT(c) FROM Consultation c " +
            "WHERE c.date >= :dateDebut " +
            "GROUP BY FUNCTION('WEEK', c.date)")
    List<Object[]> countConsultationsByWeek(@Param("dateDebut") LocalDateTime dateDebut);
}
