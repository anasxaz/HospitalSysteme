package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedecinDTO extends UserDTO{

    private String specialite;
    private String numeroOrdre;
    private BigDecimal tarifConsultation;
    private Long departementId;
    private String departementNom;


}
