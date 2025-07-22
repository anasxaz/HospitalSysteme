package com.hospital.HospitalSysteme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "basic_users")
@DiscriminatorValue("BASIC")
public class BasicUser extends User {
    // Pas besoin d'ajouter des propriétés supplémentaires
}