package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DossierMedicalDTO {

    private Long id;
    private LocalDateTime dateCreation;
    private String antecedentsMedicaux;
    private String allergies;
    private GroupeSanguin groupeSanguin;
    private Long patientId;
    private String patientNom;
    private String patientPrenom;




}
