package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
//import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByEmail(String email);
//    boolean existsById(Long id);
    List<Patient> findByGroupeSanguin(GroupeSanguin groupeSanguin);
//    Optional<Patient> findByNumeroAssurance(String numeroAssurance);
    Optional<Patient> findByNumeroAssurance(String numeroAssurance);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.allergies) LIKE LOWER(CONCAT('%', :allergie, '%'))")
    List<Patient> findByAllergieContaining(String allergie);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.nom) LIKE :searchTerm " +
            "OR LOWER(p.prenom) LIKE :searchTerm " +
            "OR LOWER(p.email) LIKE :searchTerm " +
            "OR LOWER(p.numeroAssurance) LIKE :searchTerm")
    List<Patient> searchPatients(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Patient p JOIN p.dossierMedical d WHERE d.id = :dossierMedicalId")
    Optional<Patient> findByDossierMedicalId(Long dossierMedicalId);

    @Query("SELECT DISTINCT p FROM Patient p JOIN p.planDeSoins ps WHERE ps.infirmier.id = :infirmierId")
    List<Patient> findByInfirmierId(Long infirmierId);

    @Query("SELECT COUNT(DISTINCT p) FROM Patient p " +
            "JOIN p.dossierMedical dm " +
            "JOIN dm.consultations c " +
            "JOIN c.medecin m " +
            "JOIN m.departement d " +
            "WHERE d.id = :departementId")
    int countDistinctByConsultationsMedecinDepartementId(@Param("departementId") Long departementId);


//    Page<Patient> findByNomContaining(String nom, Pageable pageable);

//    Long countByDateCreationBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT p.groupeSanguin, COUNT(p) FROM Patient p GROUP BY p.groupeSanguin")
    List<Object[]> countPatientsByGroupeSanguin();

    @Query("SELECT FUNCTION('YEAR', p.dateNaissance), COUNT(p) FROM Patient p GROUP BY FUNCTION('YEAR', p.dateNaissance)")
    List<Object[]> countPatientsByAnneeNaissance();


    // Méthode pour récupérer les patients avec filtrage
//    List<Patient> findByFiltres(Map<String, Object> filtres);
    @Query("SELECT p FROM Patient p WHERE " +
            "(:nom IS NULL OR p.nom LIKE %:nom%) AND " +
            "(:prenom IS NULL OR p.prenom LIKE %:prenom%) AND " +
            "(:dateNaissance IS NULL OR p.dateNaissance = :dateNaissance)")
    List<Patient> findByFiltres(@Param("nom") String nom,
                                @Param("prenom") String prenom,
                                @Param("dateNaissance") LocalDate dateNaissance);

    // Méthode pour récupérer les patients avec pagination pour l'exportation
    @Query("SELECT p FROM Patient p WHERE " +
            "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:prenom IS NULL OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
            "(:groupeSanguin IS NULL OR p.groupeSanguin = :groupeSanguin)")
    List<Patient> findForExport(@Param("nom") String nom,
                                @Param("prenom") String prenom,
                                @Param("groupeSanguin") String groupeSanguin);


//    @Query("SELECT p.groupeSanguin, COUNT(DISTINCT p) FROM Patient p " +
//            "JOIN Consultation c ON c.patient = p " +
//            "WHERE c.date BETWEEN :debut AND :fin " +
//            "GROUP BY p.groupeSanguin")
//    List<Object[]> countPatientsByGroupeSanguinWithConsultationsBetween(@Param("debut") LocalDateTime debut,
//                                                                        @Param("fin") LocalDateTime fin);
    @Query("SELECT p.groupeSanguin, COUNT(DISTINCT p) FROM Patient p " +
            "JOIN p.dossierMedical dm " +
            "JOIN dm.consultations c " +
            "WHERE c.date BETWEEN :debut AND :fin " +
            "GROUP BY p.groupeSanguin")
    List<Object[]> countPatientsByGroupeSanguinWithConsultationsBetween(@Param("debut") LocalDateTime debut,
                                                                        @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(p) FROM Patient p " +
            "WHERE p.dateCreation BETWEEN :debut AND :fin")
    Long countByDateCreationBetween(@Param("debut") LocalDateTime debut,
                                    @Param("fin") LocalDateTime fin);


//    default Long countByDateCreationBetween(LocalDateTime debut, LocalDateTime fin) {
//        return 0L; // Implémentation temporaire
//    }


}
