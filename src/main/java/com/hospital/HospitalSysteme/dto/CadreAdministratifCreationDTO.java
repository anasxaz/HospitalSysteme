package com.hospital.HospitalSysteme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CadreAdministratifCreationDTO extends UserCreationDTO{

    @NotBlank(message = "La fonction est obligatoire")
    private String fonction;

    @NotNull(message = "L'ID du département est obligatoire")
    private Long departementId;



}
