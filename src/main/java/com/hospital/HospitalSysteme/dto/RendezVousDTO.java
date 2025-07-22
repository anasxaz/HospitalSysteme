package com.hospital.HospitalSysteme.dto;


import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RendezVousDTO {

    private Long id;
    private LocalDateTime dateHeure;
    private Integer duree;
    private StatutRendezVous statut;
    private String motif;
    private String notes;
    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private Long medecinId;
    private String medecinNom;
    private String medecinPrenom;
    private String medecinSpecialite;

}
