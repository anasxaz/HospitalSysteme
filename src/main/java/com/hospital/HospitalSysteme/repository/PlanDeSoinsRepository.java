package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.PlanDeSoins;
import com.hospital.HospitalSysteme.entity.enums.StatutPlanDeSoins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PlanDeSoinsRepository extends JpaRepository<PlanDeSoins, Long> {

    List<PlanDeSoins> findByPatientId(Long patientId);
    List<PlanDeSoins> findByInfirmierId(Long infirmierId);
    List<PlanDeSoins> findByStatut(StatutPlanDeSoins statut);

    @Query("SELECT p FROM PlanDeSoins p WHERE p.dateDebut <= :date AND p.dateFin >= :date")
    List<PlanDeSoins> findActiveAtDate(LocalDate date);

    @Query("SELECT p FROM PlanDeSoins p WHERE p.patient.id = :patientId AND p.statut = :statut")
    List<PlanDeSoins> findByPatientIdAndStatut(Long patientId, StatutPlanDeSoins statut);

//    List<PlanDeSoins> findByDossierMedicalIdAndDateFinAfter(Long dossierMedicalId, LocalDate date);
    @Query("SELECT p FROM PlanDeSoins p WHERE p.patient.dossierMedical.id = :dossierMedicalId AND p.dateFin > :date")
    List<PlanDeSoins> findByDossierMedicalIdAndDateFinAfter(@Param("dossierMedicalId") Long dossierMedicalId, @Param("date") LocalDate date);

    @Query("SELECT p FROM PlanDeSoins p WHERE p.infirmier.id = :infirmierId AND p.patient.id = :patientId")
    List<PlanDeSoins> findByInfirmierIdAndPatientId(Long infirmierId, Long patientId);

    @Query("SELECT p FROM PlanDeSoins p WHERE p.infirmier.id = :infirmierId AND p.dateDebut <= :date AND p.dateFin >= :date")
    List<PlanDeSoins> findByInfirmierIdAndDate(Long infirmierId, LocalDate date);


    @Query("SELECT p FROM PlanDeSoins p WHERE p.statut = 'EN_COURS'")
    List<PlanDeSoins> findActifs();

    List<PlanDeSoins> findByDateDebutGreaterThanEqualAndDateFinLessThanEqual(LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT p FROM PlanDeSoins p WHERE :date BETWEEN p.dateDebut AND p.dateFin")
    List<PlanDeSoins> findByDate(LocalDate date);

    int countByStatut(StatutPlanDeSoins statut);

    int countByPatientId(Long patientId);

    int countByInfirmierId(Long infirmierId);


}
