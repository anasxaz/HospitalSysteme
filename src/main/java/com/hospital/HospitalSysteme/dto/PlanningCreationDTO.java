package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutPlanning;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanningCreationDTO {

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    // On peut conserver ce champ si on souhaite associer automatiquement
    // tous les cadres administratifs d'un département au planning
    @NotNull(message = "L'ID du département est obligatoire")
    private Long departementId;

    // Ou vous pouvez ajouter ce champ pour spécifier explicitement
    // quels cadres administratifs sont associés au planning
    private Set<Long> cadreAdministratifIds;

}
