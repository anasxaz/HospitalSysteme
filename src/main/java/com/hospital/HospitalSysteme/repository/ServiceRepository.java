package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.ServiceHospitalier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceHospitalier, Long> {

    List<ServiceHospitalier> findByCategorie(String categorie);

    List<ServiceHospitalier> findByActifTrue();

    List<ServiceHospitalier> findByActifFalse();

    List<ServiceHospitalier> findByNomContainingIgnoreCase(String nom);

    int countByCategorie(String categorie);

    @Query("SELECT s.categorie, COUNT(s) FROM ServiceHospitalier s GROUP BY s.categorie")
    List<Object[]> countByAllCategories();

    @Query("SELECT SUM(s.tarif) FROM ServiceHospitalier s WHERE s.categorie = ?1")
    BigDecimal sumTarifsByCategorie(String categorie);

    @Query("SELECT s FROM ServiceHospitalier s ORDER BY s.tarif DESC")
    List<ServiceHospitalier> findAllOrderByTarifDesc();

    /**
     * Récupère toutes les catégories distinctes qui existent réellement dans la table services
     * @return Liste des catégories uniques présentes en base de données
     */
    @Query("SELECT DISTINCT s.categorie FROM ServiceHospitalier s WHERE s.categorie IS NOT NULL ORDER BY s.categorie")
    List<String> findDistinctCategories();

}
