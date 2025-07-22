package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String posologie;
    private String instructions;
    private StatutPrescription statut;
    private Long consultationId;
    private Long medicamentId;
    private String medicamentNom;

}
