package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.AuthentificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentification", description = "API pour la gestion de l'authentification")
public class AuthController {

    private final AuthentificationService authentificationService;

//
//    @PostMapping("/register")
//    @Operation(summary = "Inscrire un nouvel utilisateur", description = "Crée un compte utilisateur dans le système")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Utilisateur créé",
//                    content = @Content(schema = @Schema(implementation = AuthenticationResponseDTO.class))),
//            @ApiResponse(responseCode = "400", description = "Données invalides ou email déjà utilisé")
//    })
//    public ResponseEntity<AuthenticationResponseDTO> register(
//            @Parameter(description = "Informations d'inscription") @Valid @RequestBody UserCreationDTO userCreationDTO) {
//        log.info("Demande d'inscription pour l'email : {}", userCreationDTO.getEmail());
//        AuthenticationResponseDTO response = authentificationService.register(userCreationDTO);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }


    @PostMapping("/login")
    @Operation(summary = "Authentifier un utilisateur",
            description = "Permet à un utilisateur de se connecter avec son email et mot de passe",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Opération réussie"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé"),
                    @ApiResponse(responseCode = "404", description = "Ressource non trouvée")
            })
    public ResponseEntity<AuthenticationResponseDTO> login(@Valid @RequestBody AuthenticationRequestDTO request) {
        log.info("Requête de connexion reçue pour l'email : {}", request.getEmail());
        AuthenticationResponseDTO response = authentificationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Déconnecter un utilisateur",
            description = "Invalide le token d'authentification de l'utilisateur",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Opération réussie"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé"),
                    @ApiResponse(responseCode = "404", description = "Ressource non trouvée")
            })
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        log.info("Requête de déconnexion reçue");
        String token = authHeader.substring(7); // Enlever "Bearer "
        authentificationService.logout(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset-request")
    @Operation(summary = "Demander une réinitialisation de mot de passe",
            description = "Envoie un email avec un lien de réinitialisation de mot de passe",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Opération réussie"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé"),
                    @ApiResponse(responseCode = "404", description = "Ressource non trouvée")
            })
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO request) {
        log.info("Requête de réinitialisation de mot de passe reçue pour l'email : {}", request.getEmail());
        authentificationService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/{token}")
    @Operation(summary = "Réinitialiser le mot de passe",
            description = "Réinitialise le mot de passe avec le token reçu par email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Opération réussie"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé"),
                    @ApiResponse(responseCode = "404", description = "Ressource non trouvée")
            })
    public ResponseEntity<Void> resetPassword(
            @PathVariable String token,
            @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        log.info("Requête de réinitialisation de mot de passe avec token reçue");
        authentificationService.resetPassword(token, passwordUpdateDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    @Operation(summary = "Changer le mot de passe",
            description = "Permet à un utilisateur connecté de changer son mot de passe",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Opération réussie"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé"),
                    @ApiResponse(responseCode = "404", description = "Ressource non trouvée")
            })
    public ResponseEntity<Void> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        log.info("Requête de changement de mot de passe reçue");
        String token = authHeader.substring(7); // Enlever "Bearer "
        Long userId = authentificationService.getUserIdFromToken(token);
        authentificationService.changePassword(userId, passwordUpdateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-token")
    @Operation(summary = "Valider un token",
            description = "Vérifie si un token est valide",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Opération réussie"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé"),
                    @ApiResponse(responseCode = "404", description = "Ressource non trouvée")
            })
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        log.info("Requête de validation de token reçue");
        String token = authHeader.substring(7); // Enlever "Bearer "
        boolean isValid = authentificationService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/user-info")
    @Operation(summary = "Obtenir les informations de l'utilisateur",
            description = "Récupère les informations de l'utilisateur à partir du token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Opération réussie"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé"),
                    @ApiResponse(responseCode = "404", description = "Ressource non trouvée")
            })
    public ResponseEntity<Object> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        log.info("Requête d'informations utilisateur reçue");
        String token = authHeader.substring(7); // Enlever "Bearer "
        Long userId = authentificationService.getUserIdFromToken(token);
        String role = authentificationService.getRoleFromToken(token);

        // Créer un objet simple avec les informations de base
        // Dans un cas réel, vous pourriez appeler un service utilisateur pour obtenir plus d'informations
        return ResponseEntity.ok(new Object() {
            public final Long id = userId;
            public final String userRole = role;
        });
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception ex) {
        log.error("Erreur dans AuthController", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Object() {
                    public final String message = ex.getMessage();
                    public final String error = "Erreur d'authentification";
                });
    }
}