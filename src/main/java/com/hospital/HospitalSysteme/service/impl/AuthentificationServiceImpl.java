package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.AuthenticationRequestDTO;
import com.hospital.HospitalSysteme.dto.AuthenticationResponseDTO;
import com.hospital.HospitalSysteme.dto.PasswordResetRequestDTO;
import com.hospital.HospitalSysteme.dto.PasswordUpdateDTO;
import com.hospital.HospitalSysteme.entity.User;
import com.hospital.HospitalSysteme.exception.AuthenticationException;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthentificationServiceImpl implements AuthentificationService {

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
                    user.getProfil().name(),    // role
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
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    @Override
    public String getRoleFromToken(String token) {
        log.info("Récupération du rôle à partir du token");
        return jwtTokenProvider.getRoleFromToken(token);
    }
}