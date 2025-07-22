package com.hospital.HospitalSysteme.dto;


import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionCreationDTO {

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotBlank(message = "La posologie est obligatoire")
    private String posologie;

    private String instructions;

    @NotNull(message = "L'ID de la consultation est obligatoire")
    private Long consultationId;

    @NotNull(message = "L'ID du médicament est obligatoire")
    private Long medicamentId;

}
