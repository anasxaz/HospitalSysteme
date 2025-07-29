//package com.hospital.HospitalSysteme.repository;
//
//import com.hospital.HospitalSysteme.entity.Prescription;
//import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.id = :consultationId")
//    List<Prescription> findByConsultationId(@Param("consultationId") Long consultationId);
//
//    List<Prescription> findByStatut(StatutPrescription statut);
//
////    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.id = :dossierMedicalId AND p.date > :date")
////    List<Prescription> findByDossierMedicalIdAndDateFinAfter(@Param("dossierMedicalId") Long dossierMedicalId, @Param("date") LocalDate date);
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.id = :dossierMedicalId AND p.date > :date")
//    List<Prescription> findByDossierMedicalIdAndDateFinAfter(@Param("dossierMedicalId") Long dossierMedicalId, @Param("date") LocalDate date);
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.medecin.id = :medecinId")
//    List<Prescription> findByMedecinId(@Param("medecinId") Long medecinId);
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.medecin.id = :medecinId AND p.consultation.dossierMedical.patient.id = :patientId")
//    List<Prescription> findByMedecinIdAndPatientId(@Param("medecinId") Long medecinId, @Param("patientId") Long patientId);
//
////    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.patient.id = :patientId")
////    List<Prescription> findByPatientId(@Param("patientId") Long patientId);
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.patient.id = :patientId")
//    List<Prescription> findByPatientId(@Param("patientId") Long patientId);
//
////    @Query("SELECT p FROM Prescription p WHERE p.date = :date")
////    List<Prescription> findByDateDebut(@Param("date") LocalDate date);
//    @Query("SELECT p FROM Prescription p WHERE p.date = :date")
//    List<Prescription> findByDateDebut(@Param("date") LocalDate date);
//
//    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.consultation.medecin.id = :medecinId")
//    int countByMedecinId(@Param("medecinId") Long medecinId);
//
//    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.consultation.dossierMedical.patient.id = :patientId")
//    int countByPatientId(@Param("patientId") Long patientId);
//
//    @Query("SELECT m.id, m.nom, COUNT(p) as count FROM Medicament m JOIN m.prescriptions p GROUP BY m.id, m.nom ORDER BY count DESC")
//    List<Object[]> findMostPrescribedMedicaments(@Param("limit") int limit);
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.id = :dossierMedicalId")
//    List<Prescription> findByConsultationDossierMedicalId(@Param("dossierMedicalId") Long dossierMedicalId);
//}
//
//
//
//


//package com.hospital.HospitalSysteme.repository;
//
//import com.hospital.HospitalSysteme.entity.Prescription;
//import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.id = :consultationId")
//    List<Prescription> findByConsultationId(@Param("consultationId") Long consultationId);
//
//    List<Prescription> findByStatut(StatutPrescription statut);
//
//    // ✅ CORRECTION 1 : Changer p.date en p.dateFin (car c'est logique pour "DateFinAfter")
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.id = :dossierMedicalId AND p.dateFin > :date")
//    List<Prescription> findByDossierMedicalIdAndDateFinAfter(@Param("dossierMedicalId") Long dossierMedicalId, @Param("date") LocalDate date);
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.medecin.id = :medecinId")
//    List<Prescription> findByMedecinId(@Param("medecinId") Long medecinId);
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.medecin.id = :medecinId AND p.consultation.dossierMedical.patient.id = :patientId")
//    List<Prescription> findByMedecinIdAndPatientId(@Param("medecinId") Long medecinId, @Param("patientId") Long patientId);
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.patient.id = :patientId")
//    List<Prescription> findByPatientId(@Param("patientId") Long patientId);
//
//    // ✅ CORRECTION 2 : Changer p.date en p.dateDebut (cohérent avec le nouveau nom du champ)
//    @Query("SELECT p FROM Prescription p WHERE p.dateDebut = :date")
//    List<Prescription> findByDateDebut(@Param("date") LocalDate date);
//
//    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.consultation.medecin.id = :medecinId")
//    int countByMedecinId(@Param("medecinId") Long medecinId);
//
//    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.consultation.dossierMedical.patient.id = :patientId")
//    int countByPatientId(@Param("patientId") Long patientId);
//
//    // ✅ CORRECTION 3 : Ajouter LIMIT dans la query (version native SQL plus facile)
//    @Query(value = "SELECT m.id, m.nom, COUNT(pm.prescription_id) as count " +
//            "FROM medicaments m " +
//            "JOIN prescription_medicament pm ON m.id = pm.medicament_id " +
//            "GROUP BY m.id, m.nom " +
//            "ORDER BY count DESC " +
//            "LIMIT :limit", nativeQuery = true)
//    List<Object[]> findMostPrescribedMedicaments(@Param("limit") int limit);
//
//    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.id = :dossierMedicalId")
//    List<Prescription> findByConsultationDossierMedicalId(@Param("dossierMedicalId") Long dossierMedicalId);
//}




package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Prescription;
import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {


    Optional<Prescription> findById(Long aLong);

    @Query("SELECT p FROM Prescription p WHERE p.consultation.id = :consultationId")
    List<Prescription> findByConsultationId(@Param("consultationId") Long consultationId);

    List<Prescription> findByStatut(StatutPrescription statut);

    // ✅ CORRECTION : Utiliser p.date au lieu de p.dateFin
    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.id = :dossierMedicalId AND p.date > :date")
    List<Prescription> findByDossierMedicalIdAndDateFinAfter(@Param("dossierMedicalId") Long dossierMedicalId, @Param("date") LocalDate date);

    @Query("SELECT p FROM Prescription p WHERE p.consultation.medecin.id = :medecinId")
    List<Prescription> findByMedecinId(@Param("medecinId") Long medecinId);

    @Query("SELECT p FROM Prescription p WHERE p.consultation.medecin.id = :medecinId AND p.consultation.dossierMedical.patient.id = :patientId")
    List<Prescription> findByMedecinIdAndPatientId(@Param("medecinId") Long medecinId, @Param("patientId") Long patientId);

    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.patient.id = :patientId")
    List<Prescription> findByPatientId(@Param("patientId") Long patientId);

    // ✅ CORRECTION : Utiliser p.date au lieu de p.dateDebut
    @Query("SELECT p FROM Prescription p WHERE p.date = :date")
    List<Prescription> findByDateDebut(@Param("date") LocalDate date);

    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.consultation.medecin.id = :medecinId")
    int countByMedecinId(@Param("medecinId") Long medecinId);

    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.consultation.dossierMedical.patient.id = :patientId")
    int countByPatientId(@Param("patientId") Long patientId);

//    @Query(value = "SELECT m.id, m.nom, COUNT(pm.prescription_id) as count " +
//            "FROM medicaments m " +
//            "JOIN prescription_medicament pm ON m.id = pm.medicament_id " +
//            "GROUP BY m.id, m.nom " +
//            "ORDER BY count DESC " +
//            "LIMIT :limit", nativeQuery = true)
//    List<Object[]> findMostPrescribedMedicaments(@Param("limit") int limit);

//    @Query("SELECT m.id, m.nom, COUNT(pm) as nombre " +
//            "FROM Medicament m " +
//            "JOIN m.prescriptions pm " +
//            "GROUP BY m.id, m.nom " +
//            "ORDER BY COUNT(pm) DESC")
//    List<Object[]> findMostPrescribedMedicaments(Pageable pageable);
//
//    // Ou version plus simple :
    @Query(value = "SELECT m.id, m.nom, COUNT(*) as nombre_prescriptions " +
            "FROM medicaments m " +
            "JOIN prescription_medicaments pm ON m.id = pm.medicament_id " +
            "GROUP BY m.id, m.nom " +
            "ORDER BY nombre_prescriptions DESC " +
            "LIMIT ?1", nativeQuery = true)
    List<Object[]> findMostPrescribedMedicaments(int limit);

    @Query("SELECT p FROM Prescription p WHERE p.consultation.dossierMedical.id = :dossierMedicalId")
    List<Prescription> findByConsultationDossierMedicalId(@Param("dossierMedicalId") Long dossierMedicalId);
}