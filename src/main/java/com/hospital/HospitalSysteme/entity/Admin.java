package com.hospital.HospitalSysteme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    private LocalDateTime derniereConnexionAdmin;

    // Stocker les permissions comme une chaîne JSON ou CSV
//    @Column(nullable = false)
//    private String permissions;

//    @Column(nullable = false)
//    private int niveauAcces;
    /*
    * Concernant le niveauAcces, c'est généralement un entier qui représente le niveau hiérarchique de l'administrateur dans le système. Par exemple :
        1 : Admin de base (accès limité)
        2 : Admin intermédiaire
        3 : Admin supérieur
        4 : Super Admin (accès complet)
    * */


}
