package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {

    boolean existsByEmail(String email);



    List<Medecin> findBySpecialite(String specialite);
    List<Medecin> findByDepartementId(Long departementId);

    @Query("SELECT m FROM Medecin m WHERE m.tarifConsultation <= :tarifMax")
    List<Medecin> findByTarifConsultationLessThanEqual(BigDecimal tarifMax);

    @Query("SELECT DISTINCT m FROM Medecin m JOIN m.rendezVous r WHERE r.dateHeure BETWEEN :debut AND :fin")
    List<Medecin> findWithAppointmentsBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT m FROM Medecin m WHERE LOWER(m.nom) LIKE :searchTerm " +
            "OR LOWER(m.prenom) LIKE :searchTerm " +
            "OR LOWER(m.email) LIKE :searchTerm " +
            "OR LOWER(m.specialite) LIKE :searchTerm")
    List<Medecin> searchMedecins(@Param("searchTerm") String searchTerm);

    @Query("SELECT m FROM Medecin m WHERE m.id NOT IN " +
            "(SELECT r.medecin.id FROM RendezVous r WHERE r.dateHeure = :dateHeure)")
    List<Medecin> findAvailableAtDateTime(LocalDateTime dateHeure);

    int countByDepartementId(Long departementId);





}
