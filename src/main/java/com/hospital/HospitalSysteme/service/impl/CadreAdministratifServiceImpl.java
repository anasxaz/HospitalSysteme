package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.*;
import com.hospital.HospitalSysteme.entity.enums.ProfilUser;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.*;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.CadreAdministratifService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CadreAdministratifServiceImpl implements CadreAdministratifService {

    // Dependency Injection
    private final RendezVousRepository rendezVousRepository;
    private final CadreAdministratifRepository cadreAdministratifRepository;
    private final DepartementRepository departementRepository;
    private final FactureRepository factureRepository;
    private final MedecinRepository medecinRepository;
    private final PatientRepository patientRepository;



    private final RendezVousMapper rendezVousMapper;
    private final CadreAdministratifMapper cadreAdministratifMapper;
    private final FactureMapper factureMapper;




//    @Override
//    public CadreAdministratifDTO createCadreAdministratif(CadreAdministratifCreationDTO cadreAdministratifCreationDTO) {
//        log.info("Création d'un nouveau cadre administratif avec l'email : {}", cadreAdministratifCreationDTO.getEmail());
//
//        // Vérifier si l'email existe déjà
//        if (cadreAdministratifRepository.existsByEmail(cadreAdministratifCreationDTO.getEmail())) {
//            throw new IllegalArgumentException("Un cadre administratif avec cet email existe déjà : " + cadreAdministratifCreationDTO.getEmail());
//        }
//
//        // Convert cadreAdministratifCreationDTO to patient JPA Entity
//        CadreAdministratif cadreAdministratif = cadreAdministratifMapper.toEntity(cadreAdministratifCreationDTO);
//
//        // cadreAdministratif JPA Entity
//        CadreAdministratif savedCadre = cadreAdministratifRepository.save(cadreAdministratif);
//
//        log.info("Cadre administratif créé avec succès avec l'ID : {}", savedCadre.getId());
//
//        // Convert saved cadre administratif JPA Entity into DTO object
//        return cadreAdministratifMapper.toDTO(savedCadre);
//    }


    // 2ème tentative :
//    @Override
//    public CadreAdministratifDTO createCadreAdministratif(CadreAdministratifCreationDTO cadreAdministratifCreationDTO) {
//        log.info("Création d'un nouveau cadre administratif avec l'email : {}", cadreAdministratifCreationDTO.getEmail());
//
//        // Vérifier si l'email existe déjà
//        if (cadreAdministratifRepository.existsByEmail(cadreAdministratifCreationDTO.getEmail())) {
//            throw new IllegalArgumentException("Un cadre administratif avec cet email existe déjà : " + cadreAdministratifCreationDTO.getEmail());
//        }
//
//        // Convert cadreAdministratifCreationDTO to cadreAdministratif JPA Entity
//        CadreAdministratif cadreAdministratif = cadreAdministratifMapper.toEntity(cadreAdministratifCreationDTO);
//
//        // AJOUTEZ CES LIGNES pour définir les valeurs par défaut
//        cadreAdministratif.setProfil(ProfilUser.PERSONNEL);
//        cadreAdministratif.setDateEmbauche(LocalDate.now());
//        cadreAdministratif.setPoste("Cadre Administratif");
//
//        // Assigner le département
//        Departement departement = departementRepository.findById(cadreAdministratifCreationDTO.getDepartementId())
//                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + cadreAdministratifCreationDTO.getDepartementId()));
//        cadreAdministratif.setDepartement(departement);
//
//        // cadreAdministratif JPA Entity
//        CadreAdministratif savedCadre = cadreAdministratifRepository.save(cadreAdministratif);
//
//        log.info("Cadre administratif créé avec succès avec l'ID : {}", savedCadre.getId());
//
//        // Convert saved cadre administratif JPA Entity into DTO object
//        return cadreAdministratifMapper.toDTO(savedCadre);
//    }


    @Override
    public CadreAdministratifDTO createCadreAdministratif(CadreAdministratifCreationDTO cadreAdministratifCreationDTO) {
        log.info("Création d'un nouveau cadre administratif avec l'email : {}", cadreAdministratifCreationDTO.getEmail());

        if (cadreAdministratifRepository.existsByEmail(cadreAdministratifCreationDTO.getEmail())) {
            throw new IllegalArgumentException("Un cadre administratif avec cet email existe déjà : " + cadreAdministratifCreationDTO.getEmail());
        }

        CadreAdministratif cadreAdministratif = cadreAdministratifMapper.toEntity(cadreAdministratifCreationDTO);

        // AJOUTEZ CES LIGNES pour définir les valeurs par défaut
        cadreAdministratif.setProfil(ProfilUser.PERSONNEL);
        cadreAdministratif.setDateEmbauche(LocalDate.now());
        cadreAdministratif.setPoste("Cadre Administratif");
        // AJOUTEZ CETTE LIGNE pour le salaire
        cadreAdministratif.setSalaire(cadreAdministratifCreationDTO.getSalaire());
        cadreAdministratif.setNiveauResponsabilite(cadreAdministratifCreationDTO.getNiveauResponsabilite());

        // Assigner le département
        Departement departement = departementRepository.findById(cadreAdministratifCreationDTO.getDepartementId())
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + cadreAdministratifCreationDTO.getDepartementId()));
        cadreAdministratif.setDepartement(departement);

        CadreAdministratif savedCadre = cadreAdministratifRepository.save(cadreAdministratif);

        log.info("Cadre administratif créé avec succès avec l'ID : {}", savedCadre.getId());

        return cadreAdministratifMapper.toDTO(savedCadre);
    }






    @Override
    public CadreAdministratifDTO getCadreAdministratifById(Long cadreAdministratifId) {
        log.info("Récupération du cadre administratif avec l'ID : {}", cadreAdministratifId);

        CadreAdministratif cadreAdministratif = cadreAdministratifRepository.findById(cadreAdministratifId).orElseThrow(
                () -> new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreAdministratifId)
        );

        return cadreAdministratifMapper.toDTO(cadreAdministratif);
    }

    @Override
    public List<CadreAdministratifDTO> getAllCadresAdministratifs() {
        log.info("Récupération de tous les cadres administratifs");

        List<CadreAdministratif> cadreAdministratifs = cadreAdministratifRepository.findAll();

        return cadreAdministratifs.stream()
                .map(cadreAdministratifMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCadreAdministratif(Long cadreAdministratifId) {
        log.info("Suppression du cadre administratif avec l'ID : {}", cadreAdministratifId);

        // vérifier l'existance du cadre administratif
        if (!cadreAdministratifRepository.existsById(cadreAdministratifId)){
            throw new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreAdministratifId);
        }

        cadreAdministratifRepository.deleteById(cadreAdministratifId);

        log.info("Cadre administratif supprimé avec succès");

    }

//    @Override
//    public RendezVousDTO createRendezVous(RendezVousCreationDTO rendezVousCreationDTO) {
//        log.info("Création d'un nouveau rendez vous ");
//
//        // Convert rendezVousCreationDTO to rendezVous JPA Entity
//        RendezVous rendezVous = rendezVousMapper.toEntity(rendezVousCreationDTO);
//
//        // rendezVous JPA Entity
//        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
//
//        log.info("Rendez vous créé avec succès!");
//
//        // Convert saved rendez vous JPA Entity into DTO object
//        return rendezVousMapper.toDTO(savedRendezVous);
//    }

    @Override
    public RendezVousDTO createRendezVous(RendezVousCreationDTO rendezVousCreationDTO) {
        log.info("Création d'un nouveau rendez vous ");

        RendezVous rendezVous = rendezVousMapper.toEntity(rendezVousCreationDTO);

        // AJOUTEZ CES LIGNES - Le mapper ne récupère pas les entités automatiquement

        // Récupérer le patient
        Patient patient = patientRepository.findById(rendezVousCreationDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé"));
        rendezVous.setPatient(patient);

        // Récupérer le médecin - C'EST ÇA QUI MANQUE !
        Medecin medecin = medecinRepository.findById(rendezVousCreationDTO.getMedecinId())
                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé"));
        rendezVous.setMedecin(medecin);

        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
        return rendezVousMapper.toDTO(savedRendezVous);
    }

    // Je ne sais pas
    // De la part de Claude
//    @Override
//    public void updateRendezVousStatut(Long rendezVousId, String statut) {
//        log.info("Mise à jour du statut du rendez-vous avec l'ID : {} vers : {}", rendezVousId, statut);
//
//        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId)
//                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId));
//
//        try {
//            StatutRendezVous nouveauStatut = StatutRendezVous.valueOf(statut.toUpperCase());
//            rendezVous.setStatut(nouveauStatut);
//            rendezVousRepository.save(rendezVous);
//
//            log.info("Statut du rendez-vous mis à jour avec succès");
//        } catch (IllegalArgumentException e) {
//            log.error("Statut de rendez-vous invalide : {}", statut, e);
//            throw new IllegalArgumentException("Statut de rendez-vous invalide : " + statut);
//        }
//
//    }

    @Override
    public void updateRendezVousStatut(Long rendezVousId, StatutRendezVous statut) {
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId));

        rendezVous.setStatut(statut);
        rendezVousRepository.save(rendezVous);
    }

    // Je ne sais pas
    // De la part de Claude
    @Override
    public List<RendezVousDTO> getRendezVousByDate(LocalDate date) {
        log.info("Récupération des rendez-vous pour la date : {}", date);

        List<RendezVous> rendezVousList = rendezVousRepository.findByDate(date);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FactureDTO createFacture(FactureCreationDTO factureCreationDTO) {
        log.info("Création d'une nouvelle facture ");

        // Convert factureCreationDTO to facture JPA Entity
        Facture facture = factureMapper.toEntity(factureCreationDTO);

        // facture JPA Entity
        Facture savedFacture = factureRepository.save(facture);

        log.info("Facture créée avec succès!");

        // Convert saved facture JPA Entity into DTO object
        return factureMapper.toDTO(savedFacture);
    }

    // Je ne sais pas
    // De la part de Claude
    @Override
    public void updateFactureStatut(Long factureId, String statut) {
        log.info("Mise à jour du statut de la facture avec l'ID : {} vers : {}", factureId, statut);

        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée avec l'ID : " + factureId));

        try {
            StatutPaiement nouveauStatut = StatutPaiement.valueOf(statut.toUpperCase());
            facture.setStatutPaiement(nouveauStatut);
            factureRepository.save(facture);

            log.info("Statut de la facture mis à jour avec succès");
        } catch (IllegalArgumentException e) {
            log.error("Statut de facture invalide : {}", statut, e);
            throw new IllegalArgumentException("Statut de facture invalide : " + statut);
        }
    }

    @Override
    public List<FactureDTO> getFacturesByStatut(StatutPaiement statut) {
        log.info("Récupération de toutes les factures");

        List<Facture> factures = factureRepository.findByStatutPaiement(statut);

        return factures.stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude
    @Override
    public void assignerCadreAdministratifADepartement(Long cadreAdministratifId, Long departementId) {
        log.info("Assignation du cadre administratif avec l'ID : {} au département avec l'ID : {}", cadreAdministratifId, departementId);

        CadreAdministratif cadreAdministratif = cadreAdministratifRepository.findById(cadreAdministratifId)
                .orElseThrow(() -> new ResourceNotFoundException("Cadre administratif non trouvé avec l'ID : " + cadreAdministratifId));

        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + departementId));

        cadreAdministratif.setDepartement(departement);
        cadreAdministratifRepository.save(cadreAdministratif);

        log.info("Cadre administratif assigné au département avec succès");
    }

    // Je ne sais pas
    // De la part de Claude
    @Override
    public List<CadreAdministratifDTO> searchCadresAdministratifs(String query) {
        log.info("Recherche de cadres administratifs avec la requête : {}", query);

        String searchTerm = "%" + query.toLowerCase() + "%";

        List<CadreAdministratif> cadresAdministratifs = cadreAdministratifRepository.searchCadresAdministratifs(searchTerm);

        return cadresAdministratifs.stream()
                .map(cadreAdministratifMapper::toDTO)
                .collect(Collectors.toList());
    }
}
