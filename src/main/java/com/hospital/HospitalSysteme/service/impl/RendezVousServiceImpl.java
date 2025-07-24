package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.RendezVousCreationDTO;
import com.hospital.HospitalSysteme.dto.RendezVousDTO;
import com.hospital.HospitalSysteme.dto.RendezVousUpdateDTO;
import com.hospital.HospitalSysteme.entity.Medecin;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.entity.RendezVous;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.ConsultationMapper;
import com.hospital.HospitalSysteme.mapper.MedecinMapper;
import com.hospital.HospitalSysteme.mapper.PrescriptionMapper;
import com.hospital.HospitalSysteme.mapper.RendezVousMapper;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.RendezVousService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RendezVousServiceImpl implements RendezVousService {

    // Dependency Injection
    private MedecinRepository medecinRepository;
    private PatientRepository patientRepository;
    private RendezVousRepository rendezVousRepository;

    private RendezVousMapper rendezVousMapper;



//    @Override
//    public RendezVousDTO createRendezVous(RendezVousCreationDTO rendezVousCreationDTO) {
//        log.info("Création d'un nouveau rendez-vous le : {}", rendezVousCreationDTO.getDateHeure());
//
//        // Convert rendezVousCreationDTO to rendez vous JPA Entity
//        RendezVous rendezVous = rendezVousMapper.toEntity(rendezVousCreationDTO);
//
//        // rendez vous JPA Entity
//        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
//
//        log.info("Rendez-vous créé avec succès avec l'ID : {}", savedRendezVous.getId());
//
//        // Convert saved rendez vous JPA Entity into DTO object
//        return rendezVousMapper.toDTO(savedRendezVous);
//    }

//    @Override
//    public RendezVousDTO createRendezVous(RendezVousCreationDTO rendezVousCreationDTO) {
//        log.info("Création d'un nouveau rendez-vous le : {}", rendezVousCreationDTO.getDateHeure());
//
//        // Récupérer le médecin et le patient à partir de leurs IDs
//        Medecin medecin = medecinRepository.findById(rendezVousCreationDTO.getMedecinId())
//                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + rendezVousCreationDTO.getMedecinId()));
//
//        Patient patient = patientRepository.findById(rendezVousCreationDTO.getPatientId())
//                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + rendezVousCreationDTO.getPatientId()));
//
//        // Convert rendezVousCreationDTO to rendez vous JPA Entity
//        RendezVous rendezVous = rendezVousMapper.toEntity(rendezVousCreationDTO);
//
//        // Set medecin and patient explicitly
//        rendezVous.setMedecin(medecin);
//        rendezVous.setPatient(patient);
//
//        // Set default status if not provided
//        if (rendezVous.getStatut() == null) {
//            rendezVous.setStatut(StatutRendezVous.PROGRAMME);
//        }
//
//        // rendez vous JPA Entity
//        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
//
//        log.info("Rendez-vous créé avec succès avec l'ID : {}", savedRendezVous.getId());
//
//        // Convert saved rendez vous JPA Entity into DTO object
//        return rendezVousMapper.toDTO(savedRendezVous);
//    }

    @Override
    public RendezVousDTO createRendezVous(RendezVousCreationDTO rendezVousCreationDTO) {
        log.info("Création d'un nouveau rendez-vous le : {}", rendezVousCreationDTO.getDateHeure());

        // Convertir DTO en entité (sans médecin et patient)
        RendezVous rendezVous = rendezVousMapper.toEntity(rendezVousCreationDTO);

        // Récupérer et définir explicitement le médecin
        Medecin medecin = medecinRepository.findById(rendezVousCreationDTO.getMedecinId())
                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + rendezVousCreationDTO.getMedecinId()));
        rendezVous.setMedecin(medecin);

        // Récupérer et définir explicitement le patient
        Patient patient = patientRepository.findById(rendezVousCreationDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + rendezVousCreationDTO.getPatientId()));
        rendezVous.setPatient(patient);

        // Sauvegarder l'entité
        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
        log.info("Rendez-vous créé avec succès avec l'ID : {}", savedRendezVous.getId());

        // Convertir l'entité sauvegardée en DTO
        return rendezVousMapper.toDTO(savedRendezVous);
    }

    @Override
    public RendezVousDTO getRendezVousById(Long rendezVousId) {
        log.info("Récupération du rendez-vous avec l'ID : {}", rendezVousId);

        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId).orElseThrow(
                () -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId)
        );

        return rendezVousMapper.toDTO(rendezVous);
    }

    @Override
    public List<RendezVousDTO> getAllRendezVous() {
        log.info("Récupération de tous les rendez-vous");

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();

        return rendezVousList.stream()
                .map((rendezVous) -> rendezVousMapper.toDTO(rendezVous))
                .collect(Collectors.toList());
    }


    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public RendezVousDTO updateRendezVous(Long rendezVousId, RendezVousUpdateDTO rendezVousUpdateDTO) {
        log.info("Mise à jour du rendez-vous avec l'ID : {}", rendezVousId);

        // Récupérer le rendez-vous existant
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId));

        // Mettre à jour les informations du rendez-vous
        rendezVousMapper.updateRendezVousFromDTO(rendezVousUpdateDTO, rendezVous);

        // Sauvegarder les modifications
        RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);

        log.info("Rendez-vous mis à jour avec succès");

        // Convertir et retourner le rendez-vous mis à jour
        return rendezVousMapper.toDTO(updatedRendezVous);
    }

    @Override
    public void deleteRendezVous(Long rendezVousId) {
        log.info("Suppression du rendez-vous avec l'ID : {}", rendezVousId);

        if (!rendezVousRepository.existsById(rendezVousId)){
            throw new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId);
        }

        rendezVousRepository.deleteById(rendezVousId);

        log.info("Rendez-vous supprimé avec succès");
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public void updateRendezVousStatut(Long rendezVousId, StatutRendezVous statut) {
        log.info("Mise à jour du statut du rendez-vous avec l'ID : {} vers : {}", rendezVousId, statut);

        // Récupérer le rendez-vous existant
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId));

        // Mettre à jour le statut
        rendezVous.setStatut(statut);

        // Sauvegarder les modifications
        rendezVousRepository.save(rendezVous);

        log.info("Statut du rendez-vous mis à jour avec succès");
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<RendezVousDTO> getRendezVousByStatut(StatutRendezVous statut) {
        log.info("Récupération des rendez-vous avec le statut : {}", statut);

        List<RendezVous> rendezVousList = rendezVousRepository.findByStatut(statut);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<RendezVousDTO> getRendezVousByPatient(Long patientId) {
        log.info("Récupération des rendez-vous pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientId(patientId);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<RendezVousDTO> getRendezVousByMedecin(Long medecinId) {
        log.info("Récupération des rendez-vous pour le médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinId(medecinId);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<RendezVousDTO> getRendezVousByDate(LocalDate date) {
        log.info("Récupération des rendez-vous pour la date : {}", date);

        // Définir le début et la fin de la journée
        LocalDateTime debutJournee = date.atStartOfDay();
        LocalDateTime finJournee = date.atTime(23, 59, 59);

        List<RendezVous> rendezVousList = rendezVousRepository.findByDateHeureBetween(debutJournee, finJournee);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<RendezVousDTO> getRendezVousBetweenDates(LocalDateTime debut, LocalDateTime fin) {
        log.info("Récupération des rendez-vous entre {} et {}", debut, fin);

        if (debut.isAfter(fin)) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }

        List<RendezVous> rendezVousList = rendezVousRepository.findByDateHeureBetween(debut, fin);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public void envoyerRappelRendezVous(Long rendezVousId) {
        log.info("Envoi d'un rappel pour le rendez-vous avec l'ID : {}", rendezVousId);

        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId));

        // Vérifier que le rendez-vous n'est pas déjà passé
        if (rendezVous.getDateHeure().isBefore(LocalDateTime.now())) {
            log.warn("Impossible d'envoyer un rappel pour un rendez-vous déjà passé : {}", rendezVousId);
            throw new IllegalStateException("Impossible d'envoyer un rappel pour un rendez-vous déjà passé");
        }

        // Vérifier que le rendez-vous n'est pas annulé
        if (rendezVous.getStatut() == StatutRendezVous.ANNULE) {
            log.warn("Impossible d'envoyer un rappel pour un rendez-vous annulé : {}", rendezVousId);
            throw new IllegalStateException("Impossible d'envoyer un rappel pour un rendez-vous annulé");
        }

        // Logique d'envoi de rappel (email, SMS, etc.)
        // Dans un système réel, vous pourriez utiliser un service de notification ici
        log.info("Rappel envoyé avec succès pour le rendez-vous avec l'ID : {}", rendezVousId);

        // Mettre à jour le statut du rendez-vous si nécessaire
        rendezVous.setStatut(StatutRendezVous.CONFIRME);
        rendezVousRepository.save(rendezVous);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public int countRendezVousByMedecinAndStatut(Long medecinId, StatutRendezVous statut) {
        log.info("Comptage des rendez-vous pour le médecin avec l'ID : {} et le statut : {}", medecinId, statut);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        return rendezVousRepository.countByMedecinIdAndStatut(medecinId, statut);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public int countRendezVousByPatientAndStatut(Long patientId, StatutRendezVous statut) {
        log.info("Comptage des rendez-vous pour le patient avec l'ID : {} et le statut : {}", patientId, statut);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        return rendezVousRepository.countByPatientIdAndStatut(patientId, statut);
    }
}
