package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandeUpdateDTO {

    private LocalDateTime dateLivraison;
    private StatutCommande statut;

}
