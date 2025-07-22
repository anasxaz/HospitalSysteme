package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {

    private String token;
    private String refreshToken;
    private Long userId;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private LocalDateTime expiresAt;

}
