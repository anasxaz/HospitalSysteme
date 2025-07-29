package com.hospital.HospitalSysteme.entity;


import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import com.hospital.HospitalSysteme.entity.enums.TypeNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String titre;

    @Column(nullable = false, name = "message")
    private String message;  // c'est le message

    @Column(nullable = false)
    private LocalDateTime dateEnvoi;

    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private StatutNotification statut = StatutNotification.NON_LUE;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private TypeNotification type;   // ← Changer String en TypeNotification

    // Relation avec User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
