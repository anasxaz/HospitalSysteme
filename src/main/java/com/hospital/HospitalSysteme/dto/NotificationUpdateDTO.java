package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUpdateDTO {
    private String titre;
    private String message;
    private StatutNotification statut;
}