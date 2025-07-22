package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.CadreAdministratif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CadreAdministratifRepository extends JpaRepository<CadreAdministratif, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT c FROM CadreAdministratif c WHERE LOWER(c.nom) LIKE :searchTerm " +
            "OR LOWER(c.prenom) LIKE :searchTerm " +
            "OR LOWER(c.email) LIKE :searchTerm " +
            "OR LOWER(c.poste) LIKE :searchTerm")
    List<CadreAdministratif> searchCadresAdministratifs(@Param("searchTerm") String searchTerm);

    List<CadreAdministratif> findByDepartementId(Long departementId);

}
