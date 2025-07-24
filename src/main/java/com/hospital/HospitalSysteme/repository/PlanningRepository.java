package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.CadreAdministratif;
import com.hospital.HospitalSysteme.entity.Planning;
import com.hospital.HospitalSysteme.entity.enums.StatutPlanning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PlanningRepository extends JpaRepository<Planning, Long> {

    List<Planning> findByStatut(StatutPlanning statut);

    List<Planning> findByDateDebutGreaterThanEqualAndDateFinLessThanEqual(LocalDate dateDebut, LocalDate dateFin);

    List<Planning> findByTitreContainingIgnoreCase(String titre);

    int countByStatut(StatutPlanning statut);

    @Query("SELECT p.statut, COUNT(p) FROM Planning p GROUP BY p.statut")
    List<Object[]> countByAllStatuts();

//    @Query("SELECT d.nom, COUNT(p) FROM Planning p JOIN p.departement d GROUP BY d.nom")
//    List<Object[]> countByDepartement();
    @Query("SELECT d.nom, COUNT(DISTINCT p) FROM Planning p JOIN p.cadreAdministratifs ca JOIN ca.departement d GROUP BY d.nom")
    List<Object[]> countByDepartement();

//    @Query("SELECT p FROM Planning p WHERE p.statut IN ('PUBLIE', 'EN_COURS') AND p.departementId = :departementId")
//    List<Planning> findActifsByDepartementId(@Param("departementId") Long departementId);
//    @Query("SELECT p FROM Planning p WHERE p.statut IN ('PUBLIE', 'EN_COURS') AND p.departement.id = :departementId")
//    List<Planning> findActifsByDepartementId(@Param("departementId") Long departementId);
//    @Query("SELECT DISTINCT p FROM Planning p JOIN p.cadreAdministratifs ca WHERE p.statut IN ('PUBLIE', 'EN_COURS') AND ca.departement.id = :departementId")
//    List<Planning> findActifsByDepartementId(@Param("departementId") Long departementId);
    @Query("SELECT DISTINCT p FROM Planning p JOIN p.cadreAdministratifs ca WHERE p.statut IN ('PUBLIE', 'EN_COURS') AND ca.departement.id = :departementId")
    List<Planning> findActifsByDepartementId(@Param("departementId") Long departementId);

//    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Planning p JOIN p.cadreAdministratifs ca WHERE ca.departement.id = :departementId AND ((p.dateDebut BETWEEN :dateDebut AND :dateFin) OR (p.dateFin BETWEEN :dateDebut AND :dateFin) OR (:dateDebut BETWEEN p.dateDebut AND p.dateFin)) AND (p.id <> :planningIdToExclude OR :planningIdToExclude IS NULL)")
//    boolean existsChevauchement(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin, @Param("departementId") Long departementId, @Param("planningIdToExclude") Long planningIdToExclude);
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Planning p JOIN p.cadreAdministratifs ca WHERE ca.departement.id = :departementId AND ((p.dateDebut BETWEEN :dateDebut AND :dateFin) OR (p.dateFin BETWEEN :dateDebut AND :dateFin) OR (:dateDebut BETWEEN p.dateDebut AND p.dateFin)) AND (p.id <> :planningIdToExclude OR :planningIdToExclude IS NULL)")
    boolean existsChevauchement(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin, @Param("departementId") Long departementId, @Param("planningIdToExclude") Long planningIdToExclude);


    @Query("SELECT p FROM Planning p WHERE " +
            "((p.dateDebut <= :fin AND p.dateFin >= :debut) OR " +
            "(p.dateDebut >= :debut AND p.dateDebut <= :fin) OR " +
            "(p.dateFin >= :debut AND p.dateFin <= :fin))")
    List<Planning> findOverlappingPlannings(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    @Query("SELECT p FROM Planning p WHERE " +
            "((p.dateDebut <= :fin AND p.dateFin >= :debut) OR " +
            "(p.dateDebut >= :debut AND p.dateDebut <= :fin) OR " +
            "(p.dateFin >= :debut AND p.dateFin <= :fin)) AND " +
            "p.id != :planningId")
    List<Planning> findOverlappingPlanningsExcluding(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin,
            @Param("planningId") Long planningId);


    List<Planning> findByCadreAdministratifsContains(CadreAdministratif cadreAdministratif);

    List<Planning> findByCadreAdministratifsIn(Collection<CadreAdministratif> cadreAdministratifs);


    List<Planning> findByCadreAdministratifsContainsAndStatutAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
            CadreAdministratif cadreAdministratif, StatutPlanning statut, LocalDate dateDebut, LocalDate dateFin);




}
