package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartementUpdateDTO {
    private String nom;
    private String description;
    private String localisation;
    private Integer capacite;
    private Long chefDepartementId;
}