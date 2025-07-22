package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import jakarta.validation.constraints.Min;
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
public class RendezVousCreationDTO {

    @NotNull(message = "La date et l'heure sont obligatoires")
    private LocalDateTime dateHeure;


    @NotNull(message = "La durée est obligatoires")
    @Min(value = 15, message = "La durée minimale est de 15 minutes")
    private Integer duree;

    @NotBlank(message = "Le motif est obligatoire")
    private String motif;

    private String notes;

    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;

    @NotNull(message = "L'ID du médecin est obligatoire")
    private Long medecinId;


}
