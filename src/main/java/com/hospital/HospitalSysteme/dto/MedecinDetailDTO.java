package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedecinDetailDTO extends MedecinDTO{

    private List<RendezVousDTO> rendezVousDTOS;
    private List<ConsultationDTO> consultationDTOS;
    private DepartementDTO departementDTO;

}
