package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PatientDetailDTO extends PatientDTO{

    private String antecedentsMedicaux;
    private DossierMedicalDTO dossierMedicalDTO;
    private List<RendezVousDTO> rendezVousDTOS;
    private List<FactureDTO> factureDTOS;
    private List<PlanDeSoinsDTO> planDeSoinsDTOS;


}
