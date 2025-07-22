package com.hospital.HospitalSysteme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "infirmiers")
@DiscriminatorValue("INFIRMIER")
public class Infirmier extends Personnel{

    @Column(length = 50, nullable = false)
    private String niveauQualification;

    // Relation avec Plan de soins
    @OneToMany(mappedBy = "infirmier", cascade = CascadeType.ALL)
    private List<PlanDeSoins> planDeSoins = new ArrayList<>();

}
