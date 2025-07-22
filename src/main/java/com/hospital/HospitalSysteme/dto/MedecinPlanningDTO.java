package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedecinPlanningDTO {

    private Long medecinId;
    private String medecinNom;
    private String medecinPrenom;
    private String specialite;
    private List<RendezVousDTO> rendezVousDTOS;
    private Map<LocalDate, List<LocalDateTime>> creneauxDisponibles;

}
