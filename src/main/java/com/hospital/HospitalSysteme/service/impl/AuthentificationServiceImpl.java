package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.AuthenticationRequestDTO;
import com.hospital.HospitalSysteme.dto.AuthenticationResponseDTO;
import com.hospital.HospitalSysteme.dto.PasswordResetRequestDTO;
import com.hospital.HospitalSysteme.dto.PasswordUpdateDTO;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.entity.User;
import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import com.hospital.HospitalSysteme.exception.AuthenticationException;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.repository.PatientRepository;
import com.hospital.HospitalSysteme.repository.UserRepository;
import com.hospital.HospitalSysteme.security.JwtTokenProvider;
import com.hospital.HospitalSysteme.service.AuthentificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hospital.HospitalSysteme.dto.UserCreationDTO;
import com.hospital.HospitalSysteme.entity.enums.ProfilUser;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthentificationServiceImpl implements AuthentificationService {

    private final PatientRepository patientRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {
        log.info("Tentative de connexion pour l'utilisateur : {}", request.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + request.getEmail()));

            String token = jwtTokenProvider.generateToken(authentication);

            // Calculer la date d'expiration (par exemple, 7 jours)
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);

            log.info("Connexion réussie pour l'utilisateur : {}", request.getEmail());

            return new AuthenticationResponseDTO(
                    token,                      // token
                    null,                       // refreshToken (null pour l'instant)
                    user.getId(),               // userId
                    user.getNom(),              // nom
                    user.getPrenom(),           // prenom
                    user.getEmail(),            // email
                    determineSpecificRole(user),    // role
                    expiresAt                   // expiresAt
            );

        } catch (Exception e) {
            log.error("Échec de l'authentification pour l'utilisateur : {}", request.getEmail(), e);
            throw new AuthenticationException("Identifiants invalides");
        }
    }

    @Override
    public void logout(String token) {
        log.info("Déconnexion de l'utilisateur");
        // Invalidation du token (si vous utilisez un stockage de tokens)
        jwtTokenProvider.invalidateToken(token);
        SecurityContextHolder.clearContext();
        log.info("Utilisateur déconnecté avec succès");
    }

    @Override
    public void requestPasswordReset(PasswordResetRequestDTO request) {
        log.info("Demande de réinitialisation de mot de passe pour l'email : {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + request.getEmail()));

        // Générer un token de réinitialisation
        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        // Envoyer un email avec le token (à implémenter avec un service d'email)
        // emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        log.info("Email de réinitialisation de mot de passe envoyé à : {}", request.getEmail());
    }

    @Override
    public void resetPassword(String token, PasswordUpdateDTO passwordUpdateDTO) {
        log.info("Réinitialisation du mot de passe avec token");

        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de réinitialisation invalide"));

        // Vérifier si le token n'a pas expiré
        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Le token de réinitialisation a expiré");
        }

        // Vérifier que les nouveaux mots de passe correspondent
        if (!passwordUpdateDTO.getNewPassword().equals(passwordUpdateDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        // Mettre à jour le mot de passe
        user.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);

        userRepository.save(user);

        log.info("Mot de passe réinitialisé avec succès pour l'utilisateur : {}", user.getEmail());
    }

    @Override
    public void changePassword(Long userId, PasswordUpdateDTO passwordUpdateDTO) {
        log.info("Changement de mot de passe pour l'utilisateur avec l'ID : {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(passwordUpdateDTO.getCurrentPassword(), user.getPassword())) {
            throw new AuthenticationException("Mot de passe actuel incorrect");
        }

        // Vérifier que les nouveaux mots de passe correspondent
        if (!passwordUpdateDTO.getNewPassword().equals(passwordUpdateDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Les nouveaux mots de passe ne correspondent pas");
        }

        // Mettre à jour le mot de passe
        user.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));

        userRepository.save(user);

        log.info("Mot de passe changé avec succès pour l'utilisateur : {}", user.getEmail());
    }

    @Override
    public boolean validateToken(String token) {
        log.info("Validation du token");
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        log.info("Récupération de l'ID utilisateur à partir du token");
        try {
            return jwtTokenProvider.getUserIdFromToken(token);
        } catch (NoSuchAlgorithmException e) {
            log.error("Erreur lors de la récupération de l'ID utilisateur à partir du token", e);
            throw new AuthenticationException("Erreur lors de la validation du token");
        }
    }

    @Override
    public String getRoleFromToken(String token) {
        log.info("Récupération du rôle à partir du token");
        try {
            return jwtTokenProvider.getRoleFromToken(token);
        } catch (NoSuchAlgorithmException e) {
            log.error("Erreur lors de la récupération du rôle à partir du token", e);
            throw new AuthenticationException("Erreur lors de la validation du token");
        }
    }

//    @Override
//    public AuthenticationResponseDTO register(UserCreationDTO userCreationDTO) {
//        log.info("Inscription d'un nouvel utilisateur avec l'email : {}", userCreationDTO.getEmail());
//
//        // Vérifier si l'email existe déjà
//        if (userRepository.existsByEmail(userCreationDTO.getEmail())) {
//            throw new IllegalArgumentException("Cet email est déjà utilisé");
//        }
//
//        // Créer un nouvel utilisateur (Patient par défaut)
//        Patient patient = new Patient();
//        patient.setNom(userCreationDTO.getNom());
//        patient.setPrenom(userCreationDTO.getPrenom());
//        patient.setEmail(userCreationDTO.getEmail());
//        patient.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));
//        patient.setTelephone(userCreationDTO.getTelephone());
//        patient.setAdresse(userCreationDTO.getAdresse());
//        patient.setDateNaissance(userCreationDTO.getDateNaissance());
//        patient.setGenre(userCreationDTO.getGenre());
//        patient.setProfil(ProfilUser.PATIENT); // Par défaut, les nouveaux inscrits sont des patients
//
//        // Définir les champs obligatoires de Patient
//        patient.setAllergies("Aucune"); // Valeur par défaut
//        patient.setAntecedentsMedicaux("Aucun"); // Valeur par défaut
//        patient.setContactUrgenceNom(""); // Valeur par défaut
//        patient.setContactUrgenceTelephone(""); // Valeur par défaut
//        patient.setGroupeSanguin(GroupeSanguin.INCONNU); // Valeur par défaut
//        patient.setNumeroAssurance(""); // Valeur par défaut
//
//        // Sauvegarder l'utilisateur
//        Patient savedPatient = patientRepository.save(patient);
//
//        try {
//            // Créer un token pour l'utilisateur
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            userCreationDTO.getEmail(),
//                            userCreationDTO.getPassword() // Utiliser le mot de passe non encodé ici
//                    )
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            String token = jwtTokenProvider.generateToken(authentication);
//
//            // Calculer la date d'expiration
//            LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
//
//            log.info("Inscription réussie pour l'utilisateur : {}", userCreationDTO.getEmail());
//
//            return new AuthenticationResponseDTO(
//                    token,
//                    null,
//                    savedPatient.getId(),
//                    savedPatient.getNom(),
//                    savedPatient.getPrenom(),
//                    savedPatient.getEmail(),
//                    savedPatient.getProfil().name(),
//                    expiresAt
//            );
//        } catch (NoSuchAlgorithmException e) {
//            log.error("Erreur lors de la génération du token pour le nouvel utilisateur", e);
//            throw new AuthenticationException("Erreur lors de l'inscription");
//        }
//    }

    private String determineSpecificRole(User user) {
        if (user instanceof com.hospital.HospitalSysteme.entity.Admin) {
            return "ADMIN";
        } else if (user instanceof com.hospital.HospitalSysteme.entity.Medecin) {
            return "MEDECIN";
        } else if (user instanceof com.hospital.HospitalSysteme.entity.Infirmier) {
            return "INFIRMIER";
        } else if (user instanceof com.hospital.HospitalSysteme.entity.CadreAdministratif) {
            return "CADRE_ADMINISTRATIF";
        } else if (user instanceof com.hospital.HospitalSysteme.entity.Patient) {
            return "PATIENT";
        }
        return "PERSONNEL";
    }
}