package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByNomContainingOrPrenomContaining(String nom, String prenom);
    @Query("SELECT u FROM User u WHERE u.derniereConnexion < :date")
    List<User> findInactiveUsers(LocalDateTime date);
    Optional<User> findByResetPasswordToken(String token);


}
