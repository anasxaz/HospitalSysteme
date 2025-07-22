package com.hospital.HospitalSysteme.entity;


import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
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
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statutPaiement;

    @Column(length = 50, nullable = false)
    private String methodePaiement;



    @CreationTimestamp
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateMiseAJour;

    // Relation avec Patient
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Relation avec Cadre Administratif
    @ManyToOne
    @JoinColumn(name = "cadre_id", nullable = false)
    private CadreAdministratif cadreAdministratif;

    // Relation avec Service
    @ManyToMany(mappedBy = "factures")
    private Set<ServiceHospitalier> serviceHospitaliers = new HashSet<>();



//    @ManyToMany
//    @JoinTable(
//            name = "facture_service",
//            joinColumns = @JoinColumn(name = "facture_id"),
//            inverseJoinColumns = @JoinColumn(name = "service_id")
//    )
//    private List<Service> services = new ArrayList<>();

}
