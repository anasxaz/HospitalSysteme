package com.hospital.HospitalSysteme.repository;

import com.hospital.HospitalSysteme.entity.Facture;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    List<Facture> findByPatientId(Long patientId);
    List<Facture> findByStatutPaiement(StatutPaiement statutPaiement);
    List<Facture> findByCadreAdministratifId(Long cadreAdministratifId);

    @Query("SELECT f FROM Facture f WHERE f.date BETWEEN :dateDebut AND :dateFin")
    List<Facture> findByDateBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    @Query("SELECT SUM(f.montantTotal) FROM Facture f WHERE f.statutPaiement = :statut")
    BigDecimal sumMontantTotalByStatutPaiement(StatutPaiement statut);

//    @Query("SELECT f FROM Facture f JOIN f.services s WHERE s.id = :serviceId")
//    List<Facture> findByServiceId(Long serviceId);
    @Query("SELECT f FROM Facture f JOIN f.serviceHospitaliers s WHERE s.id = :serviceId")
    List<Facture> findByServiceId(@Param("serviceId") Long serviceId);
    List<Facture> findByPatientIdAndStatutPaiement(Long patientId, StatutPaiement statutPaiement);


    List<Facture> findByMethodePaiement(String methodePaiement);

    int countByStatutPaiement(StatutPaiement statutPaiement);

    @Query("SELECT SUM(f.montantTotal) FROM Facture f")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT SUM(f.montantTotal) FROM Facture f WHERE f.date BETWEEN ?1 AND ?2")
    BigDecimal calculateRevenueByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);

    @Query("SELECT f.methodePaiement, SUM(f.montantTotal) FROM Facture f GROUP BY f.methodePaiement")
    List<Object[]> getRevenueByMethodePaiement();

    @Query("SELECT f.statutPaiement, COUNT(f) FROM Facture f GROUP BY f.statutPaiement")
    List<Object[]> getFactureCountByStatut();



    @Query("SELECT SUM(f.montantTotal) FROM Facture f WHERE f.date BETWEEN :debut AND :fin")
    BigDecimal sumMontantTotalByDateBetween(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(f.montantTotal) FROM Facture f WHERE f.statutPaiement = 'PAYE' AND f.date BETWEEN :debut AND :fin")
    BigDecimal sumMontantPayeByDateBetween(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT f.statutPaiement, COUNT(f) FROM Facture f GROUP BY f.statutPaiement")
    List<Object[]> countFacturesByStatut();

    @Query("SELECT FUNCTION('MONTH', f.date), SUM(f.montantTotal) FROM Facture f WHERE FUNCTION('YEAR', f.date) = :annee GROUP BY FUNCTION('MONTH', f.date)")
    List<Object[]> sumMontantByMois(@Param("annee") int annee);

//    @Query("SELECT s.nom, SUM(f.montantTotal) FROM Facture f JOIN f.services s GROUP BY s.nom")
//    List<Object[]> sumMontantByService();
    @Query("SELECT s.nom, SUM(f.montantTotal) FROM Facture f JOIN f.serviceHospitaliers s GROUP BY s.nom")
    List<Object[]> sumMontantByService();


    List<Facture> findByDateBetweenAndStatutPaiement(LocalDateTime debut, LocalDateTime fin, StatutPaiement statutPaiement);

//    @Query("SELECT SUM(f.montantTotal) FROM Facture f " +
//            "WHERE f.medecin.id = :medecinId " +
//            "AND f.dateCreation BETWEEN :debut AND :fin")
//    BigDecimal sumMontantByMedecinIdAndDateBetween(@Param("medecinId") Long medecinId,
//                                                   @Param("debut") LocalDateTime debut,
//                                                   @Param("fin") LocalDateTime fin);
    @Query("SELECT SUM(f.montantTotal) FROM Facture f " +
            "JOIN f.patient p " +
            "JOIN p.dossierMedical dm " +
            "JOIN dm.consultations c " +
            "WHERE c.medecin.id = :medecinId AND f.dateCreation BETWEEN :debut AND :fin")
    BigDecimal sumMontantByMedecinIdAndDateBetween(@Param("medecinId") Long medecinId,
                                                   @Param("debut") LocalDateTime debut,
                                                   @Param("fin") LocalDateTime fin);

    // Si vous avez une relation avec département dans Facture :
//    @Query("SELECT SUM(f.montantTotal) FROM Facture f " +
//            "WHERE f.medecin.departement.id = :departementId " +
//            "AND f.dateCreation BETWEEN :debut AND :fin")
//    BigDecimal sumMontantByDepartementAndDateBetween(@Param("departementId") Long departementId,
//                                                     @Param("debut") LocalDateTime debut,
//                                                     @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(f.montantTotal) FROM Facture f " +
            "JOIN f.patient p " +
            "JOIN p.dossierMedical dm " +
            "JOIN dm.consultations c " +
            "JOIN c.medecin m " +
            "WHERE m.departement.id = :departementId AND f.dateCreation BETWEEN :debut AND :fin")
    BigDecimal sumMontantByDepartementAndDateBetween(@Param("departementId") Long departementId,
                                                     @Param("debut") LocalDateTime debut,
                                                     @Param("fin") LocalDateTime fin);

}
