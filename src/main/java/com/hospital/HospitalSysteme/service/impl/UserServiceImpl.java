package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.PasswordChangeDTO;
import com.hospital.HospitalSysteme.dto.UserDTO;
import com.hospital.HospitalSysteme.dto.UserUpdateDTO;
import com.hospital.HospitalSysteme.entity.User;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.UserMapper;
import com.hospital.HospitalSysteme.repository.UserRepository;
import com.hospital.HospitalSysteme.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j  // Pour la journalisation (logging)
public class UserServiceImpl implements UserService {

    // Dependency injection
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDTO getUserById(Long id) {

        log.info("Récupération de l'utilisateur avec l'ID : {}", id);

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id)
        );

        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {

        log.info("Récupération de tous les utilisateurs");

        List<User> users = userRepository.findAll();

        return users.stream()
                .map((user) -> userMapper.toDTO(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {

        log.info("Modification de tous les infos de l'utilisateur avec l'ID : {}", id);

        // Retrieve data from the user
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id)
        );

//        // Update/Store data
//        UserDTO userDTO = new UserDTO();
//        userDTO.setAdresse(userUpdateDTO.getAdresse());
//        userDTO.setGenre(userUpdateDTO.getGenre());
//        userDTO.setNom(userUpdateDTO.getNom());
//        userDTO.setPrenom(userUpdateDTO.getPrenom());
//        userDTO.setTelephone(userUpdateDTO.getTelephone());
//        userDTO.setDateNaissance(userUpdateDTO.getDateNaissance());

        // Mettre à jour l'utilisateur avec les données du DTO
        userMapper.updateUserFromDTO(userUpdateDTO, user);

        // Sauvegarder l'utilisateur mis à jour
        User updatedUser = userRepository.save(user);

        // Convertir et retourner le DTO
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        log.info("Suppression de l'utilisateur avec l'ID : {}", id);

        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserByEmail(String email) {

        log.info("Récupération de l'utilisateur avec l'email : {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + email)
        );

        return userMapper.toDTO(user);
    }

    @Override
    public boolean existsByEmail(String email) {

        log.info("Vérifier l'existance de l'utilisateur avec l'email : {}", email);

        return userRepository.existsByEmail(email);
    }

    @Override
    public LocalDateTime updateLastLogin(Long userId) {

        log.info("Modification du last login de l'utilisateur avec l'ID : {}", userId);

        // Retrieve data from the user
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + userId)
        );

        // Update last login
        LocalDateTime now = LocalDateTime.now();
        user.setDerniereConnexion(now);

        // save the user
        userRepository.save(user);

        return now;
    }

    @Override
    public void changePassword(Long userId, PasswordChangeDTO passwordChangeDTO) {

        log.info("Changement du password de l'utilisateur avec l'ID : {}", userId);

        // Retrieve the user
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + userId)
        );

        // Vérifier que l'ancien mot de passe est correct
        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect");
        }

        // Vérifier que les nouveaux mots de passe correspondent
        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Les nouveaux mots de passe ne correspondent pas");
        }

        // Encoder et définir le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));

        // Sauvegarder l'utilisateur
        userRepository.save(user);
    }
}
