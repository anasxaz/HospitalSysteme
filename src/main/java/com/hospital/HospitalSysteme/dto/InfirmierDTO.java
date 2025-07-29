package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InfirmierDTO extends UserDTO{

    private String niveauQualification;
    private String numeroOrdre;
    private Long departementId;
    private String departementNom;
    private BigDecimal salaire; // AJOUTEZ CETTE LIGNE


}
