package com.hospital.HospitalSysteme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class ServiceHospitalier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    private Boolean actif = true;

    @Column(nullable = false)
    private BigDecimal tarif;

    @Column(nullable = false)
    private BigDecimal cout;

    @Column(length = 50, nullable = false)
    private String categorie;

    @CreationTimestamp
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateMiseAJour;

    @ManyToMany
    @JoinTable(
            name = "facture_services",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "facture_id")
    )
    private Set<Facture> factures = new HashSet<>();



//    @ManyToMany(mappedBy = "services")
//    private List<Facture> factures = new ArrayList<>();


}
