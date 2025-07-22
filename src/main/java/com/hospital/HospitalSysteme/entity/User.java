package com.hospital.HospitalSysteme.entity;

import com.hospital.HospitalSysteme.entity.enums.GenreUser;
import com.hospital.HospitalSysteme.entity.enums.ProfilUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")

// Pour rendre cette Entité comme une classe qu'on peut hériter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 100, nullable = false)
    private String nom;

    @Column(length = 100, nullable = false)
    private String prenom;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private LocalDate dateNaissance;    // Date <=> LocalDate  :  pour les dates sans heure (comme date de naissance)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenreUser genre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfilUser profil;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime dateCreation;    // TIMESTAMP <=> LocalDateTime  :  pour les dates avec heure (comme date de rendez-vous)

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime derniereConnexion;


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Notification> notifications = new ArrayList<>();

    // Relation avec Notification
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)    // Toujours si on a OneToMany, on fait une liste, mais dans le mappedBy on fait le nom ou on va recevoir User dans la class Notification.
    private List<Notification> notifications = new ArrayList<>();


    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;



}




