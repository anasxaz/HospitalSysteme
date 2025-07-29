package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CadreAdministratifDTO extends UserDTO{

    private String fonction;
    private String niveauResponsabilite;
    private Long departementId;
    private String departementNom;

    // AJOUTEZ CE CHAMP
    private BigDecimal salaire;
}
