package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartementDTO {

    private Long id;
    private String nom;
    private String description;
    private String localisation;
    private Integer capacite;
    private LocalDateTime dateCreation;

}
