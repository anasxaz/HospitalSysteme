package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Commande;
import com.hospital.HospitalSysteme.entity.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByCadreAdministratifId(Long cadreAdministratifId);
    List<Commande> findByStatut(StatutCommande statut);



    List<Commande> findByFournisseurContainingIgnoreCase(String fournisseur);

    List<Commande> findByDateCommandeBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    int countByStatut(StatutCommande statut);

    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.statut = ?1")
    BigDecimal sumMontantTotalByStatut(StatutCommande statut);

    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.fournisseur LIKE %?1%")
    BigDecimal sumMontantTotalByFournisseur(String fournisseur);

    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.dateCommande BETWEEN ?1 AND ?2")
    BigDecimal sumMontantTotalByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);

    @Query("SELECT c.statut, COUNT(c) FROM Commande c GROUP BY c.statut")
    List<Object[]> countByAllStatuts();

    @Query("SELECT c.fournisseur, SUM(c.montantTotal) FROM Commande c GROUP BY c.fournisseur")
    List<Object[]> sumMontantTotalByAllFournisseurs();

    @Query("SELECT c FROM Commande c GROUP BY c.fournisseur ORDER BY SUM(c.montantTotal) DESC")
    List<Commande> findTopFournisseursByMontant();

}
