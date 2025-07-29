//package com.hospital.HospitalSysteme.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class PrescriptionUpdateDTO {
//    private String instructions;
//    private LocalDate dateDebut;
//    private LocalDate dateFin;
//    private Long consultationId;
//    private Long medecinId;
//    private Long patientId;
//    private List<Long> medicamentIds;
//}

package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionUpdateDTO {
    private String instructions;
    private String posologie;           // ✅ GARDER
    private Long consultationId;
    private List<Long> medicamentIds;   // ✅ GARDER pour cohérence

    // ❌ SUPPRIMER : dateDebut, dateFin, medecinId, patientId
    // Ces champs n'ont plus de sens avec la nouvelle logique
}
