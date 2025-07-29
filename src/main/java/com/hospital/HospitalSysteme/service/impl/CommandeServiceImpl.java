package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.CommandeCreationDTO;
import com.hospital.HospitalSysteme.dto.CommandeDTO;
import com.hospital.HospitalSysteme.dto.CommandeUpdateDTO;
import com.hospital.HospitalSysteme.entity.CadreAdministratif;
import com.hospital.HospitalSysteme.entity.Commande;
import com.hospital.HospitalSysteme.entity.Notification;
import com.hospital.HospitalSysteme.entity.PlanDeSoins;
import com.hospital.HospitalSysteme.entity.enums.StatutCommande;
import com.hospital.HospitalSysteme.entity.enums.StatutNotification;
import com.hospital.HospitalSysteme.entity.enums.TypeNotification;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.CommandeMapper;
import com.hospital.HospitalSysteme.mapper.PlanDeSoinsMapper;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.CommandeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class CommandeServiceImpl implements CommandeService {

    // Dependency Injection
    private PatientRepository patientRepository;
    private PlanDeSoinsRepository planDeSoinsRepository;
    private InfirmierRepository infirmierRepository;
    private CommandeRepository commandeRepository;
    private CadreAdministratifRepository cadreAdministratifRepository;


    private PlanDeSoinsMapper planDeSoinsMapper;
    private CommandeMapper commandeMapper;




//    @Override
//    public CommandeDTO createCommande(CommandeCreationDTO commandeCreationDTO) {
//        log.info("Création d'une nouvelle commande le : {}", commandeCreationDTO.getDateCommande());
//        // ✅ Générer automatiquement la date de commande
//        Commande commande = commandeMapper.toEntity(commandeCreationDTO);
//        commande.setDateCommande(LocalDateTime.now()); // ← AUTO-GÉNÉRÉE
//
//        // Vérifier si la date de livraison est spécifiée et si elle est valide
//        if (commandeCreationDTO.getDateLivraison() != null &&
//                commandeCreationDTO.getDateLivraison().isBefore(commandeCreationDTO.getDateCommande())) {
//            throw new IllegalArgumentException("La date de livraison ne peut pas être antérieure à la date de la commande");
//        }
//
//        // Vérifier que le cadre administratif existe
//        if (!cadreAdministratifRepository.existsById(commandeCreationDTO.getCadreAdministratifId())) {
//            throw new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + commandeCreationDTO.getCadreAdministratifId());
//        }
//
//        // Convert commandeCreationDTO to commande JPA Entity
//        Commande commande = commandeMapper.toEntity(commandeCreationDTO);
//
//        // Définir le statut par défaut si non spécifié
//        if (commande.getStatut() == null) {
//            commande.setStatut(StatutCommande.EN_ATTENTE);
//        }
//
//        // plan de soins JPA Entity
//        Commande savedCommande = commandeRepository.save(commande);
//
//        log.info("Commande créée avec succès avec l'ID : {}", savedCommande.getId());
//
//        // Convert savedPlanDeSoins JPA Entity into DTO object
//        return commandeMapper.toDTO(savedCommande);
//    }

    // 2ème tentative :
//    @Override
//    public CommandeDTO createCommande(CommandeCreationDTO commandeCreationDTO) {
//        log.info("Création d'une nouvelle commande : {}", commandeCreationDTO.getReference());
//
//        // ✅ Générer automatiquement la date de commande
//        Commande commande = commandeMapper.toEntity(commandeCreationDTO);
//        commande.setDateCommande(LocalDateTime.now()); // ← AUTO-GÉNÉRÉE
//
//        // Vérifier le cadre administratif
//        if (!cadreAdministratifRepository.existsById(commandeCreationDTO.getCadreAdministratifId())) {
//            throw new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + commandeCreationDTO.getCadreAdministratifId());
//        }
//
//        // Statut par défaut
//        if (commande.getStatut() == null) {
//            commande.setStatut(StatutCommande.EN_ATTENTE);
//        }
//
//        Commande savedCommande = commandeRepository.save(commande);
//        return commandeMapper.toDTO(savedCommande);
//    }

    @Override
    public CommandeDTO createCommande(CommandeCreationDTO commandeCreationDTO) {
        log.info("Création d'une nouvelle commande : {}", commandeCreationDTO.getReference());

        // ✅ Récupérer le cadre administratif complet
        CadreAdministratif cadreAdministratif = cadreAdministratifRepository.findById(commandeCreationDTO.getCadreAdministratifId())
                .orElseThrow(() -> new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + commandeCreationDTO.getCadreAdministratifId()));

        // Convert DTO to Entity
        Commande commande = commandeMapper.toEntity(commandeCreationDTO);

        // ✅ Assigner l'entité complète
        commande.setCadreAdministratif(cadreAdministratif);

        // ✅ Auto-générer la date de commande
        commande.setDateCommande(LocalDateTime.now());

        // Statut par défaut
        if (commande.getStatut() == null) {
            commande.setStatut(StatutCommande.EN_ATTENTE);
        }

        Commande savedCommande = commandeRepository.save(commande);
        return commandeMapper.toDTO(savedCommande);
    }


    @Override
    public CommandeDTO getCommandeById(Long commandeId) {
        log.info("Récupération de la commande avec l'ID : {}", commandeId);

        Commande commande = commandeRepository.findById(commandeId).orElseThrow(
                () -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + commandeId)
        );

        return commandeMapper.toDTO(commande);
    }

    @Override
    public CommandeDTO updateCommande(Long commandeId, CommandeUpdateDTO commandeUpdateDTO) {
        log.info("Mise à jour de la commande avec l'ID : {}", commandeId);

        // Récupérer la commande existante
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + commandeId));

        // Mettre à jour les informations de la commande
        commandeMapper.updateCommandeFromDTO(commandeUpdateDTO, commande);

        // Sauvegarder les modifications
        Commande updatedCommande = commandeRepository.save(commande);

        log.info("Commande mis à jour avec succès");

        // Convertir et retourner le plan de soins mis à jour
        return commandeMapper.toDTO(updatedCommande);
    }

    @Override
    public void deleteCommande(Long commandeId) {
        log.info("Suppression de la commande avec l'ID : {}", commandeId);

        if (!commandeRepository.existsById(commandeId)){
            throw new ResourceNotFoundException("Commande non trouvée avec l'ID : " + commandeId);
        }

        commandeRepository.deleteById(commandeId);

        log.info("Commande supprimée avec succès");

    }

    @Override
    public List<CommandeDTO> getAllCommandes() {
        log.info("Récupération de toutes les commandes");

        List<Commande> commandes = commandeRepository.findAll();

        return commandes.stream()
                .map((commande) -> commandeMapper.toDTO(commande))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommandeDTO> getCommandesByStatut(StatutCommande statut) {
        log.info("Récupération des commandes avec le statut : {}", statut);

        List<Commande> commandes = commandeRepository.findByStatut(statut);

        return commandes.stream()
                .map(commandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommandeDTO> getCommandesByCadreAdministratif(Long cadreAdministratifId) {
        log.info("Récupération des commandes pour le cadre administratif avec l'ID : {}", cadreAdministratifId);

        // Vérifier que le cadre administratif existe
        if (!cadreAdministratifRepository.existsById(cadreAdministratifId)) {
            throw new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreAdministratifId);
        }

        List<Commande> commandes = commandeRepository.findByCadreAdministratifId(cadreAdministratifId);

        return commandes.stream()
                .map(commandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommandeDTO> getCommandesByFournisseur(String fournisseur) {
        log.info("Récupération des commandes pour le fournisseur : {}", fournisseur);

        List<Commande> commandes = commandeRepository.findByFournisseurContainingIgnoreCase(fournisseur);

        return commandes.stream()
                .map(commandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommandeDTO> getCommandesByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        log.info("Récupération des commandes entre le {} :  et le : {}", dateDebut, dateFin);

        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début ne peut pas être postérieure à la date de fin");
        }

        List<Commande> commandes = commandeRepository.findByDateCommandeBetween(dateDebut, dateFin);

        return commandes.stream()
                .map(commandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommandeDTO changerStatutCommande(Long commandeId, StatutCommande nouveauStatut) {
        log.info("Changer le statut de la commande avec l'ID : {} vers {}", commandeId, nouveauStatut);

        // Récupérer la commande
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + commandeId));

        // Mettre à jour le statut
        commande.setStatut(nouveauStatut);

        // Sauvegarder les modifications
        Commande updatedCommande = commandeRepository.save(commande);

        log.info("Statut de la commande mis à jour avec succès");

        return commandeMapper.toDTO(updatedCommande);
    }

//    @Override
//    public CommandeDTO enregistrerLivraison(Long commandeId, LocalDateTime dateLivraison) {
//        log.info("Enregistrement de la date de la livraison pour la commande d'ID : {} dans la date : {}", commandeId, dateLivraison);
//
//        // Récupérer la commande en vérifiant son existance
//        Commande commande = commandeRepository.findById(commandeId).orElseThrow(
//                () -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + commandeId)
//        );
//
//        // ajouter la date de livraison
//        commande.setDateLivraison(dateLivraison);
//        // modifier le statut vers LIVREE
//        commande.setStatut(StatutCommande.LIVREE);
//
//        // sauvegarder les modifications
//        Commande savedCommande = commandeRepository.save(commande);
//
//        // Convert to DTO
//        return commandeMapper.toDTO(savedCommande);
//    }


    @Override
    public CommandeDTO enregistrerLivraison(Long commandeId) {  // ← SUPPRIMER le paramètre date
        log.info("Enregistrement de la livraison pour la commande d'ID : {}", commandeId);

        // Récupérer la commande
        Commande commande = commandeRepository.findById(commandeId).orElseThrow(
                () -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + commandeId)
        );

        // ✅ DATE AUTOMATIQUE = MAINTENANT
        commande.setDateLivraison(LocalDateTime.now());
        commande.setStatut(StatutCommande.LIVREE);

        Commande savedCommande = commandeRepository.save(commande);
        return commandeMapper.toDTO(savedCommande);
    }


    @Override
    public CommandeDTO annulerCommande(Long commandeId) {
        log.info("Mise à jour du statut de la commande d'ID : {} qui sera marquer comme annulée", commandeId);

        // Récupérer la commande existante
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + commandeId));

        // Mettre à jour le statut de la commande
        commande.setStatut(StatutCommande.ANNULEE);

        // Sauvegarder les modifications
        Commande updatedCommande = commandeRepository.save(commande);

        log.info("Annulation de la commande avec succès");

        // Convertir et retourner la commande mis à jour
        return commandeMapper.toDTO(updatedCommande);
    }

    @Override
    public int countCommandesByStatut(StatutCommande statut) {
        log.info("Comptage des commandes pour le statut : {}", statut);

        return commandeRepository.countByStatut(statut);
    }

    @Override
    public BigDecimal calculateTotalMontantByStatut(StatutCommande statut) {
        log.info("Calcul du montant total pour le statut {}", statut);

        BigDecimal montantTotal = commandeRepository.sumMontantTotalByStatut(statut);

        // Si aucun résultat n'est trouvé, retourner zéro
        return montantTotal != null ? montantTotal : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateTotalMontantByFournisseur(String fournisseur) {
        log.info("Calcul du montant total pour le fournisseur {}", fournisseur);

        BigDecimal montantTotal = commandeRepository.sumMontantTotalByFournisseur(fournisseur);

        // Si aucun résultat n'est trouvé, retourner zéro
        return montantTotal != null ? montantTotal : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateTotalMontantByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        log.info("Calcul du montant total entre le {} et le {}", dateDebut, dateFin);

        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début ne peut pas être postérieure à la date de fin");
        }

        BigDecimal montantTotal = commandeRepository.sumMontantTotalByDateRange(dateDebut, dateFin);

        // Si aucun résultat n'est trouvé, retourner zéro
        return montantTotal != null ? montantTotal : BigDecimal.ZERO;
    }

    @Override
    public Map<StatutCommande, Integer> countCommandesByAllStatuts() {
        log.info("Comptage des commandes pour tous les types de statuts");


        List<Object[]> results = commandeRepository.countByAllStatuts();

        Map<StatutCommande, Integer> countByAllStatuts = new HashMap<>();
        for (Object[] result : results) {
            StatutCommande statut = (StatutCommande) result[0];
            Long count = (Long) result[1];
            countByAllStatuts.put(statut, count.intValue());
        }

        return countByAllStatuts;
    }


    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public Map<String, BigDecimal> calculateTotalMontantByAllFournisseurs() {
        log.info("Calcul du montant total pour tous les fournisseurs");

        List<Object[]> results = commandeRepository.sumMontantTotalByAllFournisseurs();

        Map<String, BigDecimal> montantByFournisseur = new HashMap<>();
        for (Object[] result : results) {
            String fournisseur = (String) result[0];
            BigDecimal montant = (BigDecimal) result[1];
            montantByFournisseur.put(fournisseur, montant);
        }

        return montantByFournisseur;
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<CommandeDTO> getTopFournisseursByMontant(int limit) {
        log.info("Récupération des {} principaux fournisseurs par montant", limit);

        List<Commande> topFournisseurs = commandeRepository.findTopFournisseursByMontant();

        // Limiter le nombre de résultats si nécessaire
        if (limit > 0 && limit < topFournisseurs.size()) {
            topFournisseurs = topFournisseurs.subList(0, limit);
        }

        return commandeMapper.toDTOList(topFournisseurs);
    }
}
