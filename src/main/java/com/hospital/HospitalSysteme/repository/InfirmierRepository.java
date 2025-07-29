package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Infirmier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InfirmierRepository extends JpaRepository<Infirmier, Long> {

    boolean existsByEmail(String email);
    List<Infirmier> findByNiveauQualification(String niveauQualification);
    List<Infirmier> findByDepartementId(Long departementId);

    // AJOUTEZ cette méthode pour recherche partielle
    @Query("SELECT i FROM Infirmier i WHERE LOWER(i.niveauQualification) LIKE LOWER(CONCAT('%', :niveauQualification, '%'))")
    List<Infirmier> findByNiveauQualificationContainingIgnoreCase(@Param("niveauQualification") String niveauQualification);


    @Query("SELECT i FROM Infirmier i WHERE LOWER(i.nom) LIKE :searchTerm " +
            "OR LOWER(i.prenom) LIKE :searchTerm " +
            "OR LOWER(i.email) LIKE :searchTerm " +
            "OR LOWER(i.niveauQualification) LIKE :searchTerm")
    List<Infirmier> searchInfirmiers(@Param("searchTerm") String searchTerm);

//    int countByDepartementId(Long departementId);

    // Dans InfirmierRepository.java, ajoute :
    @Query("SELECT COUNT(i) FROM Infirmier i WHERE i.departement.id = :departementId")
    Long countByDepartementId(@Param("departementId") Long departementId);

}
