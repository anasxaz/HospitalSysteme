package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FactureDetailDTO extends FactureDTO{

    private List<ServiceDTO> serviceDTOS;
    private PatientDTO patientDTO;
    private CadreAdministratifDTO cadreAdministratifDTO;

}
