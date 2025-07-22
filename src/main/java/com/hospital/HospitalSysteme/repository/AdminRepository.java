package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // à voir est ce que je vais le laisser ou pas
    List<Admin> findByNiveauAcces(int niveauAcces);

}
