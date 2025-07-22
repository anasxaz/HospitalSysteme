package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConsultationDetailDTO extends ConsultationDTO{

    private List<PrescriptionDTO> prescriptions;
    private String patientNom;
    private String patientPrenom;

}
