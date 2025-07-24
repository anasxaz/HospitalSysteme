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
public class MedicamentDTO {

    private Long id;
    private String nom;
    private String description;
    private String dosage;
    private String fabricant;
    private String categorie;
    private String contreIndications;
    private String effetsSecondaires;
//    private BigDecimal prix;
//    private int stockDisponible;
    private Boolean ordonnanceRequise;


}
