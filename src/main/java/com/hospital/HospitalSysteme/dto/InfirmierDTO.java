package com.hospital.HospitalSysteme.dto;

import lombok.*;

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


}
