package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DossierMedicalDetailDTO extends DossierMedicalDTO{

    private List<ConsultationDTO> consultationDTOS;
    private List<PrescriptionDTO> prescriptionDTOS;
    private PatientDTO patient;

}
