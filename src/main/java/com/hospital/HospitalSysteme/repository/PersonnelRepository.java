package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
}
