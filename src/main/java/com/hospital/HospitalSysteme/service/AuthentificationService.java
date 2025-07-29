package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.User;

public interface AuthentificationService {
    // Authentification
    AuthenticationResponseDTO login(AuthenticationRequestDTO request);
    void logout(String token);

    // Gestion des mots de passe
    void requestPasswordReset(PasswordResetRequestDTO request);
    void resetPassword(String token, PasswordUpdateDTO passwordUpdateDTO);
    void changePassword(Long userId, PasswordUpdateDTO passwordUpdateDTO);

    // Vérification de token
    boolean validateToken(String token);
    Long getUserIdFromToken(String token);
    String getRoleFromToken(String token);
//    String determineSpecificRole(User user);

    // Pour se connecter
//    AuthenticationResponseDTO register(UserCreationDTO userCreationDTO);
}