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

    @Query("SELECT s.categorie, COUNT(s) FROM Service s GROUP BY s.categorie")
    List<Object[]> countByAllCategories();

    @Query("SELECT SUM(s.tarif) FROM Service s WHERE s.categorie = ?1")
    BigDecimal sumTarifsByCategorie(String categorie);

    @Query("SELECT s FROM Service s ORDER BY s.tarif DESC")
    List<ServiceHospitalier> findAllOrderByTarifDesc();

}
