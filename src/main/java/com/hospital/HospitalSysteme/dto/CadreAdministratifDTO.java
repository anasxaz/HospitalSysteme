package com.hospital.HospitalSysteme.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CadreAdministratifDTO extends UserDTO{

    private String fonction;
    private Long departementId;
    private String departementNom;

}
