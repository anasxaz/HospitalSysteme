package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    List<Medicament> findByNomContaining(String nom);

    List<Medicament> findByNomContainingIgnoreCase(String nom);

    List<Medicament> findByCategorie(String categorie);

    List<Medicament> findByFabricant(String fabricant);

    List<Medicament> findByOrdonnanceRequise(Boolean ordonnanceRequise);

    List<Medicament> findByPrescriptionsId(Long prescriptionId);

    int countByCategorie(String categorie);

    int countByFabricant(String fabricant);

    @Query("SELECT DISTINCT m.categorie FROM Medicament m WHERE m.categorie IS NOT NULL ORDER BY m.categorie")
    List<String> findDistinctCategories();

    @Query("SELECT DISTINCT m.fabricant FROM Medicament m WHERE m.fabricant IS NOT NULL ORDER BY m.fabricant")
    List<String> findDistinctFabricants();

}
