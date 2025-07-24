package com.hospital.HospitalSysteme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentUpdateDTO {
    private String nom;  // Ajouté
    private String description;
    private String dosage;
    private String fabricant;
    private String categorie;
    private String effetsSecondaires;  // Ajouté
    private String contreIndications;  // Ajouté
    private boolean ordonnanceRequise;
}