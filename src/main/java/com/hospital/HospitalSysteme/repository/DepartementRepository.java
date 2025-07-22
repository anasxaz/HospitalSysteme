package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, Long> {

    boolean existsByNom(String nom);

    Optional<Departement> findByNom(String nom);

    @Query("SELECT d FROM Departement d JOIN d.personnels p GROUP BY d.id ORDER BY COUNT(p) DESC")
    List<Departement> findAllOrderByPersonnelCountDesc();

    @Query("SELECT d FROM Departement d WHERE d.localisation = :localisation")
    List<Departement> findByLocalisation(String localisation);

    @Query("SELECT d FROM Departement d WHERE LOWER(d.nom) LIKE :searchTerm " +
            "OR LOWER(d.description) LIKE :searchTerm")
    List<Departement> searchDepartements(@Param("searchTerm") String searchTerm);




//    @Query("SELECT d.id, d.nom, COUNT(m) FROM Departement d LEFT JOIN d.medecins m GROUP BY d.id, d.nom")
//    List<Object[]> countMedecinsByDepartement();

    @Query("SELECT d.id, d.nom, COUNT(p) FROM Departement d LEFT JOIN d.personnels p WHERE p.class = Medecin GROUP BY d.id, d.nom")
//    @Query("SELECT d.id, d.nom, COUNT(p) FROM Departement d LEFT JOIN d.personnels p WHERE TYPE(p) = 'MEDECIN' GROUP BY d.id, d.nom")
    List<Object[]> countMedecinsByDepartement();

//    @Query("SELECT d.id, d.nom, COUNT(i) FROM Departement d LEFT JOIN d.infirmiers i GROUP BY d.id, d.nom")
//    List<Object[]> countInfirmiersByDepartement();

    @Query("SELECT d.id, d.nom, COUNT(p) FROM Departement d LEFT JOIN d.personnels p WHERE p.class = Infirmier GROUP BY d.id, d.nom")
//    @Query("SELECT d.id, d.nom, COUNT(p) FROM Departement d LEFT JOIN d.personnels p WHERE TYPE(p) = 'INFIRMIER' GROUP BY d.id, d.nom")
    List<Object[]> countInfirmiersByDepartement();

}
