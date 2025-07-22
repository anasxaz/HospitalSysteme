package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutPlanDeSoins;
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
public class PlanDeSoinsCreationDTO {

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    private String instructions;

    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;

    @NotNull(message = "L'ID de l'infirmier est obligatoire")
    private Long infirmierId;


}
