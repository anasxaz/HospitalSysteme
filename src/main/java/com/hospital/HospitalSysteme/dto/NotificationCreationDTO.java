package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import com.hospital.HospitalSysteme.entity.enums.TypeNotification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreationDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "Le message est obligatoire")
    private String message;

    @NotNull(message = "Le type est obligatoire")
    private TypeNotification type;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long userId;



}
