package com.hospital.HospitalSysteme.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminCreationDTO extends UserCreationDTO {
    // Pas de champs supplémentaires nécessaires pour la création
    // Le champ derniereConnexionAdmin sera initialisé à null
}
