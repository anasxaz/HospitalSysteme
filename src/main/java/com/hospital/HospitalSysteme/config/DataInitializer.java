package com.hospital.HospitalSysteme.config;

import com.hospital.HospitalSysteme.entity.Admin;
import com.hospital.HospitalSysteme.entity.User;
import com.hospital.HospitalSysteme.entity.enums.GenreUser;
import com.hospital.HospitalSysteme.entity.enums.ProfilUser;
import com.hospital.HospitalSysteme.repository.AdminRepository;
import com.hospital.HospitalSysteme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si un admin existe déjà
        if (userRepository.findByEmail("admin@hospital.com").isEmpty()) {
            // Créer un utilisateur admin
            Admin admin = new Admin();
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setEmail("admin@hospital.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setAdresse("Adresse Admin");
            admin.setTelephone("0600000000");
            admin.setGenre(GenreUser.M);
            admin.setDateNaissance(LocalDate.of(1990, 1, 1));
            admin.setProfil(ProfilUser.ADMIN);
            admin.setDateCreation(LocalDateTime.now());
            admin.setDerniereConnexion(LocalDateTime.now());
            admin.setDerniereConnexionAdmin(LocalDateTime.now());

            adminRepository.save(admin);

            System.out.println("Admin créé avec succès : admin@hospital.com / admin123");
        }
    }
}