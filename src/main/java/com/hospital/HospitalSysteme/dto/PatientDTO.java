package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PatientDTO extends UserDTO{

    private String numeroAssurance;
    private GroupeSanguin groupeSanguin;
    private String allergies;
    private String contactUrgenceNom;
    private String contactUrgenceTelephone;

}
