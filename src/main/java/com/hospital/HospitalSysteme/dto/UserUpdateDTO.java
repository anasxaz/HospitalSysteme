package com.hospital.HospitalSysteme.dto;

import com.hospital.HospitalSysteme.entity.enums.GenreUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;
    private GenreUser genre;

}
