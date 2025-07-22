package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.FactureCreationDTO;
import com.hospital.HospitalSysteme.dto.FactureDTO;
import com.hospital.HospitalSysteme.dto.FactureDetailDTO;
import com.hospital.HospitalSysteme.entity.Facture;
import com.hospital.HospitalSysteme.entity.PlanDeSoins;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.CadreAdministratifMapper;
import com.hospital.HospitalSysteme.mapper.FactureMapper;
import com.hospital.HospitalSysteme.mapper.PlanDeSoinsMapper;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.FacturationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FacturationServiceImpl implements FacturationService {

    // Dependency Injection
    private PatientRepository patientRepository;
    private PlanDeSoinsRepository planDeSoinsRepository;
    private InfirmierRepository infirmierRepository;
    private FactureRepository factureRepository;
    private CadreAdministratifRepository cadreAdministratifRepository;


    private PlanDeSoinsMapper planDeSoinsMapper;
    private FactureMapper factureMapper;
    private CadreAdministratifMapper cadreAdministratifMapper;




    @Override
    public FactureDTO createFacture(FactureCreationDTO factureCreationDTO) {
        log.info("Création d'une nouvelle facture le : {}", factureCreationDTO.getDate());

        // Convert factureCreationDTO to facture JPA Entity
        Facture facture = factureMapper.toEntity(factureCreationDTO);

        // Générer un numéro de facture unique
        facture.setNumero("FACT-" + System.currentTimeMillis());

        // Définir le statut par défaut si non spécifié
        if (facture.getStatutPaiement() == null) {
            facture.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        }

        // facture JPA Entity
        Facture savedFacture = factureRepository.save(facture);

        log.info("Facture créée avec succès avec l'ID : {}", savedFacture.getId());

        // Convert savedFacture JPA Entity into DTO object
        return factureMapper.toDTO(savedFacture);
    }

    @Override
    public FactureDTO getFactureById(Long factureId) {
        log.info("Récupération de la facture avec l'ID : {}", factureId);

        Facture facture = factureRepository.findById(factureId).orElseThrow(
                () -> new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId)
        );

        return factureMapper.toDTO(facture);
    }

    @Override
    public FactureDetailDTO getFactureDetailById(Long factureId) {
        log.info("Récupération de la facture avec l'ID : {} avec son détail", factureId);

        Facture facture = factureRepository.findById(factureId).orElseThrow(
                () -> new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId)
        );

        return factureMapper.toDetailDTO(facture);
    }

    @Override
    public FactureDTO updateStatutPaiement(Long factureId, StatutPaiement statutPaiement) {
        log.info("Update du statut de la facture avec l'ID : {} vers {}", factureId, statutPaiement);

        // Il faut vérifier que la facture existe d'abord
        if(!factureRepository.existsById(factureId)){
            throw new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId);
        }

        // Il faut retrieve la facture
        Facture facture = factureRepository.findById(factureId).orElseThrow(
                () -> new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId)
        );

        // on modifie à l'aide du setter
        facture.setStatutPaiement(statutPaiement);

        // On sauvegarde le tout!
        Facture savedFacture = factureRepository.save(facture);

        return factureMapper.toDTO(savedFacture);
    }

    @Override
    public FactureDTO updateMethodePaiement(Long factureId, String methodePaiement) {
        log.info("Update de la méthode de paiement de la facture avec l'ID : {} vers {}", factureId, methodePaiement);

        // Il faut vérifier que la facture existe d'abord
        if(!factureRepository.existsById(factureId)){
            throw new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId);
        }

        // Il faut retrieve la facture
        Facture facture = factureRepository.findById(factureId).orElseThrow(
                () -> new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId)
        );

        // on modifie à l'aide du setter
        facture.setMethodePaiement(methodePaiement);

        // On sauvegarde le tout!
        Facture savedFacture = factureRepository.save(facture);

        return factureMapper.toDTO(savedFacture);
    }

    @Override
    public void deleteFacture(Long factureId) {
        log.info("Suppression de la facture avec l'ID : {}", factureId);

        if (!factureRepository.existsById(factureId)){
            throw new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId);
        }

        factureRepository.deleteById(factureId);

        log.info("Facture supprimée avec succès");

    }

    @Override
    public List<FactureDTO> getAllFactures() {
        log.info("Récupération de toutes les factures");

        List<Facture> factures = factureRepository.findAll();

        return factures.stream()
                .map((facture) -> factureMapper.toDTO(facture))
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getFacturesByPatientId(Long patientId) {
        log.info("Récupération des factures pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        List<Facture> factures = factureRepository.findByPatientId(patientId);

        return factures.stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getFacturesByCadreAdministratifId(Long cadreAdministratifId) {
        log.info("Récupération des factures pour le cadre administratif avec l'ID : {}", cadreAdministratifId);

        // Vérifier que le cadre administratif existe
        if (!cadreAdministratifRepository.existsById(cadreAdministratifId)) {
            throw new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreAdministratifId);
        }

        List<Facture> factures = factureRepository.findByCadreAdministratifId(cadreAdministratifId);

        return factures.stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getFacturesByStatutPaiement(StatutPaiement statutPaiement) {
        log.info("Récupération des factures avec le statut : {}", statutPaiement);

        List<Facture> factures = factureRepository.findByStatutPaiement(statutPaiement);

        return factures.stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getFacturesByMethodePaiement(String methodePaiement) {
        log.info("Récupération des factures avec la méthode de paiement : {}", methodePaiement);

        List<Facture> factures = factureRepository.findByMethodePaiement(methodePaiement);

        return factures.stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getFacturesByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        log.info("Récupération des factures entre le : {} et le : {}", dateDebut, dateFin);

        List<Facture> factures = factureRepository.findByDateBetween(dateDebut, dateFin);

        return factures.stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }


    // J'ai fait un test, mais la logique est incorrecte, donc voici la recommandation de Claude pour s'y inspirer
    @Override
    public FactureDTO enregistrerPaiement(Long factureId, BigDecimal montant, String methodePaiement) {
        log.info("Enregistrement du paiement pour la facture d'ID : {} avec le montant : {} et avec la méthode de paiement : {}", factureId, montant, methodePaiement);

        // Récupérer la facture
        Facture facture = factureRepository.findById(factureId).orElseThrow(
                () -> new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId)
        );

        // Vérifier que la facture n'est pas déjà payée ou annulée
        if (facture.getStatutPaiement() == StatutPaiement.PAYEE || facture.getStatutPaiement() == StatutPaiement.ANNULEE) {
            throw new IllegalStateException("Impossible d'enregistrer un paiement pour une facture " + facture.getStatutPaiement());
        }

        // Vérifier que le montant payé correspond au montant total de la facture
        if (montant.compareTo(facture.getMontantTotal()) != 0) {
            throw new IllegalArgumentException("Le montant payé doit être égal au montant total de la facture");
        }

        // Mettre à jour le statut et la méthode de paiement
        facture.setStatutPaiement(StatutPaiement.PAYEE);
        facture.setMethodePaiement(methodePaiement);

        // Sauvegarder les modifications
        Facture savedFacture = factureRepository.save(facture);

        log.info("Paiement enregistré avec succès!");
        return factureMapper.toDTO(savedFacture);
    }

    @Override
    public int countFacturesByStatutPaiement(StatutPaiement statutPaiement) {
        log.info("Comptage des factures pour le statut : {}", statutPaiement);

        return factureRepository.countByStatutPaiement(statutPaiement);
    }


    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public BigDecimal calculateTotalRevenue() {
        log.info("Calcul du revenu total");

        BigDecimal totalRevenue = factureRepository.calculateTotalRevenue();

        // Si aucun résultat n'est trouvé, retourner zéro
        return totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public BigDecimal calculateRevenueByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        log.info("Calcul du revenu entre le {} et le {}", dateDebut, dateFin);

        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début ne peut pas être postérieure à la date de fin");
        }

        BigDecimal revenue = factureRepository.calculateRevenueByDateRange(dateDebut, dateFin);

        // Si aucun résultat n'est trouvé, retourner zéro
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public Map<String, BigDecimal> getRevenueByMethodePaiement() {
        log.info("Récupération des revenus par méthode de paiement");

        List<Object[]> results = factureRepository.getRevenueByMethodePaiement();

        Map<String, BigDecimal> revenueByMethod = results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (BigDecimal) result[1],
                        (existing, replacement) -> existing
                ));

        return revenueByMethod;
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public Map<String, Integer> getFactureCountByStatut() {
        log.info("Récupération du nombre de factures par statut");

        List<Object[]> results = factureRepository.getFactureCountByStatut();

        Map<String, Integer> countByStatut = results.stream()
                .collect(Collectors.toMap(
                        result -> ((StatutPaiement) result[0]).name(),
                        result -> ((Long) result[1]).intValue(),
                        (existing, replacement) -> existing
                ));

        return countByStatut;
    }
}
