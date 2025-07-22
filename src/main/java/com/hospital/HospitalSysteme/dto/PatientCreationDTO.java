package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PatientCreationDTO extends UserCreationDTO{

    private String numeroAssurance;
    private GroupeSanguin groupeSanguin;
    private String allergies;
    private String antecedentsMedicaux;
    private String contactUrgenceNom;
    private String contactUrgenceTelephone;

}
