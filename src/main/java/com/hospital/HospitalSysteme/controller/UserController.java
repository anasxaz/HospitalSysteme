package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.PasswordChangeDTO;
import com.hospital.HospitalSysteme.dto.UserDTO;
import com.hospital.HospitalSysteme.dto.UserUpdateDTO;
import com.hospital.HospitalSysteme.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
@Tag(name = "User Controller", description = "API pour la gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par son ID", description = "Renvoie les détails d'un utilisateur spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID de l'utilisateur à récupérer") @PathVariable Long id) {
        log.info("Requête GET pour récupérer l'utilisateur avec l'ID: {}", id);
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les utilisateurs", description = "Renvoie la liste de tous les utilisateurs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Requête GET pour récupérer tous les utilisateurs");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Récupérer un utilisateur par son email", description = "Renvoie les détails d'un utilisateur spécifique par email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserEmail(#email)")
    public ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(description = "Email de l'utilisateur à récupérer") @PathVariable String email) {
        log.info("Requête GET pour récupérer l'utilisateur avec l'email: {}", email);
        UserDTO userDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour les informations d'un utilisateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID de l'utilisateur à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour de l'utilisateur") @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("Requête PUT pour mettre à jour l'utilisateur avec l'ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer") @PathVariable Long id) {
        log.info("Requête DELETE pour supprimer l'utilisateur avec l'ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Changer le mot de passe d'un utilisateur", description = "Met à jour le mot de passe d'un utilisateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mot de passe changé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id,
            @Parameter(description = "Données de changement de mot de passe") @Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        log.info("Requête PUT pour changer le mot de passe de l'utilisateur avec l'ID: {}", id);
        userService.changePassword(id, passwordChangeDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/last-login")
    @Operation(summary = "Mettre à jour la dernière connexion", description = "Met à jour la date de dernière connexion d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Date de dernière connexion mise à jour"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<LocalDateTime> updateLastLogin(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        log.info("Requête PUT pour mettre à jour la dernière connexion de l'utilisateur avec l'ID: {}", id);
        LocalDateTime lastLogin = userService.updateLastLogin(id);
        return ResponseEntity.ok(lastLogin);
    }

    @GetMapping("/check-email")
    @Operation(summary = "Vérifier si un email existe", description = "Vérifie si un email est déjà utilisé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vérification effectuée")
    })
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email à vérifier") @RequestParam String email) {
        log.info("Requête GET pour vérifier si l'email existe: {}", email);
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}