package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.PasswordChangeDTO;
import com.hospital.HospitalSysteme.dto.UserDTO;
import com.hospital.HospitalSysteme.dto.UserUpdateDTO;

import java.time.LocalDateTime;
import java.util.List;


public interface UserService {

    // Opérations CRUD de base
    // Consulter son profil
    UserDTO getUserById(Long id);
    // Récupérer tous les utilisateurs (pour l'administration)
    List<UserDTO> getAllUsers();
    // Mettre à jour son profil
    UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);
    // Supprimer un utilisateur
    void deleteUser(Long id);



    // Opérations spécifiques
    // Récupérer un utilisateur par email (utile pour l'authentification)
    UserDTO getUserByEmail(String email);
    // Vérifier si un email existe déjà (utile lors de l'inscription)
    boolean existsByEmail(String email);
    // Mettre à jour la date de dernière connexion
    LocalDateTime updateLastLogin(Long userId);
    // Changer son password
    void changePassword(Long userId, PasswordChangeDTO passwordChangeDTO);

}
