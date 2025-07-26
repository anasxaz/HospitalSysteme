package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.DossierMedical;
import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {

    Optional<DossierMedical> findByPatientId(Long patientId);


    boolean existsByPatientId(Long patientId);

    List<DossierMedical> findByPatientNomContainingIgnoreCase(String nom);

    List<DossierMedical> findByPatientGroupeSanguin(GroupeSanguin groupeSanguin);

    List<DossierMedical> findByAllergiesContainingIgnoreCase(String allergies);



    List<DossierMedical> findByGroupeSanguin(String groupeSanguin);





}
