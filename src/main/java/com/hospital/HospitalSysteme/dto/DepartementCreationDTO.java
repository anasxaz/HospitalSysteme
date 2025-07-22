package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartementCreationDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;
    private String localisation;
    private Integer capacite;

}
