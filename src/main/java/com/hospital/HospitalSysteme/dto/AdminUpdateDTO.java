package com.hospital.HospitalSysteme.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminUpdateDTO extends UserUpdateDTO {
    // Pas besoin d'inclure derniereConnexionAdmin ici
    // Ce champ sera mis à jour automatiquement lors de la connexion
}
