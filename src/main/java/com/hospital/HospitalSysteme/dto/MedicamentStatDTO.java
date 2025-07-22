package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentStatDTO {
    private Long id;
    private String nom;
    private int nombrePrescriptions;
}