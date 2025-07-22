package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import com.hospital.HospitalSysteme.entity.enums.TypeNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    private String titre;
    private String message;
    private LocalDateTime dateEnvoi;
    private StatutNotification statut;
    private TypeNotification type;
    private Long userId;
    private String userNom;
    private String userPrenom;


}
