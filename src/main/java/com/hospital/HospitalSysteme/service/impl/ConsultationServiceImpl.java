package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.*;
import com.hospital.HospitalSysteme.service.ConsultationService;

import java.time.LocalDate;
import java.util.List;

import com.hospital.HospitalSysteme.dto.RendezVousCreationDTO;
import com.hospital.HospitalSysteme.dto.RendezVousDTO;
import com.hospital.HospitalSysteme.dto.RendezVousUpdateDTO;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.ConsultationMapper;
import com.hospital.HospitalSysteme.mapper.MedecinMapper;
import com.hospital.HospitalSysteme.mapper.PrescriptionMapper;
import com.hospital.HospitalSysteme.mapper.RendezVousMapper;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.RendezVousService;
//import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ConsultationServiceImpl implements ConsultationService {

    // Dependency Injection
    private MedecinRepository medecinRepository;
    private PatientRepository patientRepository;
    private RendezVousRepository rendezVousRepository;
    private ConsultationRepository consultationRepository;
    private PrescriptionRepository prescriptionRepository;
    private DossierMedicalRepository dossierMedicalRepository;

    private MedecinMapper medecinMapper;
    private RendezVousMapper rendezVousMapper;
    private ConsultationMapper consultationMapper;
    private PrescriptionMapper prescriptionMapper;




//    @Override
//    @Transactional
//    public ConsultationDTO createConsultation(ConsultationCreationDTO consultationCreationDTO) {
//        log.info("Création d'une nouvelle consultation le : {}", consultationCreationDTO.getDate());
//
//        // Convert consultationCreationDTO to consultation JPA Entity
//        Consultation consultation = consultationMapper.toEntity(consultationCreationDTO);
//
//        // consultation JPA Entity
//        Consultation savedConsultation = consultationRepository.save(consultation);
//
//        log.info("Consultation créée avec succès avec l'ID : {}", savedConsultation.getId());
//
//        // Convert saved consultation JPA Entity into DTO object
//        return consultationMapper.toDTO(savedConsultation);
//    }


    // 2ème tentative
//    @Override
//    @Transactional  // ✅ AJOUTÉ !
//    public ConsultationDTO createConsultation(ConsultationCreationDTO consultationCreationDTO) {
//        log.info("Création d'une nouvelle consultation le : {}", consultationCreationDTO.getDate());
//
//        // ✅ 1. Récupérer et vérifier le médecin
//        Medecin medecin = medecinRepository.findById(consultationCreationDTO.getMedecinId())
//                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + consultationCreationDTO.getMedecinId()));
//
//        // ✅ 2. Récupérer et vérifier le dossier médical
//        DossierMedical dossierMedical = dossierMedicalRepository.findById(consultationCreationDTO.getDossierMedicalId())
//                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + consultationCreationDTO.getDossierMedicalId()));
//
//        // ✅ 3. Récupérer le rendez-vous si spécifié
//        RendezVous rendezVous = null;
//        if (consultationCreationDTO.getRendezVousId() != null) {
//            rendezVous = rendezVousRepository.findById(consultationCreationDTO.getRendezVousId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + consultationCreationDTO.getRendezVousId()));
//        }
//
//        // ✅ 4. Créer l'entité Consultation via le mapper
//        Consultation consultation = consultationMapper.toEntity(consultationCreationDTO);
//
//        // ✅ 5. DÉFINIR MANUELLEMENT LES RELATIONS (CRUCIAL !)
//        consultation.setMedecin(medecin);
//        consultation.setDossierMedical(dossierMedical);
//
//        if (rendezVous != null) {
//            consultation.setRendezVous(rendezVous);
//        }
//
//        // ✅ 6. Debug log pour vérifier
//        log.info("Consultation créée avec - Médecin ID: {}, Dossier ID: {}, RDV ID: {}",
//                consultation.getMedecin().getId(),
//                consultation.getDossierMedical().getId(),
//                rendezVous != null ? rendezVous.getId() : "aucun");
//
//        // ✅ 7. Sauvegarder
//        Consultation savedConsultation = consultationRepository.save(consultation);
//
//        log.info("Consultation créée avec succès avec l'ID : {}", savedConsultation.getId());
//
//        return consultationMapper.toDTO(savedConsultation);
//    }


//    @Override
//    @Transactional
//    public ConsultationDTO createConsultation(ConsultationCreationDTO consultationCreationDTO) {
//        log.info("Création d'une nouvelle consultation le : {}", consultationCreationDTO.getDate());
//
//        // ✅ 1. Récupérer et vérifier le médecin
//        Medecin medecin = medecinRepository.findById(consultationCreationDTO.getMedecinId())
//                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + consultationCreationDTO.getMedecinId()));
//        log.info("Médecin trouvé : {} {}", medecin.getNom(), medecin.getPrenom());
//
//        // ✅ 2. Récupérer et vérifier le dossier médical
//        DossierMedical dossierMedical = dossierMedicalRepository.findById(consultationCreationDTO.getDossierMedicalId())
//                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + consultationCreationDTO.getDossierMedicalId()));
//        log.info("Dossier médical trouvé pour le patient : {} {}",
//                dossierMedical.getPatient().getNom(), dossierMedical.getPatient().getPrenom());
//
//        // ✅ 3. Créer l'entité Consultation MANUELLEMENT
//        Consultation consultation = new Consultation();
//        consultation.setDate(consultationCreationDTO.getDate());
//        consultation.setSymptomes(consultationCreationDTO.getSymptomes());
//        consultation.setDiagnostic(consultationCreationDTO.getDiagnostic());
//        consultation.setNotes(consultationCreationDTO.getNotes());
//        consultation.setRecommandations(""); // Valeur par défaut car obligatoire dans votre entité
//
//        // ✅ 4. DÉFINIR LES RELATIONS MANUELLEMENT
//        consultation.setMedecin(medecin);
//        consultation.setDossierMedical(dossierMedical);
//
//        // ✅ 5. Debug log pour vérifier que les relations sont bien définies
//        log.info("Relations définies - Médecin ID: {}, Dossier médical ID: {}",
//                consultation.getMedecin().getId(),
//                consultation.getDossierMedical().getId());
//
//        // ✅ 6. Sauvegarder
//        Consultation savedConsultation = consultationRepository.save(consultation);
//
//        log.info("Consultation créée avec succès avec l'ID : {}", savedConsultation.getId());
//
//        // ✅ 7. Convertir et retourner
//        return consultationMapper.toDTO(savedConsultation);
//    }


    @Override
    @Transactional
    public ConsultationDTO createConsultation(ConsultationCreationDTO consultationCreationDTO) {
        log.info("Création d'une nouvelle consultation pour le médecin ID: {}", consultationCreationDTO.getMedecinId());

        // ✅ 1. Récupérer et vérifier le médecin
        Medecin medecin = medecinRepository.findById(consultationCreationDTO.getMedecinId())
                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + consultationCreationDTO.getMedecinId()));

        // ✅ 2. Récupérer et vérifier le dossier médical
        DossierMedical dossierMedical = dossierMedicalRepository.findById(consultationCreationDTO.getDossierMedicalId())
                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + consultationCreationDTO.getDossierMedicalId()));

        // ✅ 3. Récupérer le rendez-vous si spécifié
        RendezVous rendezVous = null;
        if (consultationCreationDTO.getRendezVousId() != null) {
            rendezVous = rendezVousRepository.findById(consultationCreationDTO.getRendezVousId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + consultationCreationDTO.getRendezVousId()));
        }

        // ✅ 4. Créer l'entité Consultation MANUELLEMENT avec date automatique
        Consultation consultation = new Consultation();
        consultation.setDate(LocalDateTime.now()); // ✅ DATE AUTOMATIQUE !
        consultation.setSymptomes(consultationCreationDTO.getSymptomes());
        consultation.setDiagnostic(consultationCreationDTO.getDiagnostic());
        consultation.setNotes(consultationCreationDTO.getNotes());
        consultation.setRecommandations("Recommandations à définir"); // Valeur par défaut

        // ✅ 5. DÉFINIR LES RELATIONS
        consultation.setMedecin(medecin);
        consultation.setDossierMedical(dossierMedical);

        // ✅ 6. ASSOCIER LE RENDEZ-VOUS (IMPORTANT !)
        if (rendezVous != null) {
            consultation.setRendezVous(rendezVous);
            log.info("Rendez-vous ID {} associé à la consultation", rendezVous.getId());
        }

        // ✅ 7. Sauvegarder
        Consultation savedConsultation = consultationRepository.save(consultation);

        log.info("Consultation créée avec succès avec l'ID : {}", savedConsultation.getId());

        return consultationMapper.toDTO(savedConsultation);
    }


    @Override
    public ConsultationDTO getConsultationById(Long consultationId) {
        log.info("Récupération du consultation avec l'ID : {}", consultationId);

        Consultation consultation = consultationRepository.findById(consultationId).orElseThrow(
                () -> new ResourceNotFoundException("Consultation non trouvé avec l'ID : " + consultationId)
        );

        return consultationMapper.toDTO(consultation);
    }

    @Override
    public List<ConsultationDTO> getAllConsultations() {
        log.info("Récupération de toutes les consultations");

        List<Consultation> consultations = consultationRepository.findAll();

        return consultations.stream()
                .map((consultation) -> consultationMapper.toDTO(consultation))
                .collect(Collectors.toList());
    }

    @Override
    public ConsultationDTO updateConsultation(Long consultationId, ConsultationUpdateDTO consultationUpdateDTO) {
        log.info("Mise à jour de la consultation avec l'ID : {}", consultationId);

        // Récupérer la consultation existante
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId));

        // Mettre à jour les informations de la consultation
        consultationMapper.updateConsultationFromDTO(consultationUpdateDTO, consultation);

        // Sauvegarder les modifications
        Consultation updatedConsultation = consultationRepository.save(consultation);

        log.info("Consultation mis à jour avec succès");

        // Convertir et retourner la consultation mis à jour
        return consultationMapper.toDTO(updatedConsultation);
    }

    @Override
    public void deleteConsultation(Long consultationId) {
        log.info("Suppression de la consultation avec l'ID : {}", consultationId);

        if (!consultationRepository.existsById(consultationId)){
            throw new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId);
        }

        consultationRepository.deleteById(consultationId);

        log.info("Consultation supprimée avec succès");

    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<ConsultationDTO> getConsultationsByPatient(Long patientId) {
        log.info("Récupération des consultations pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        List<Consultation> consultations = consultationRepository.findByDossierMedicalPatientId(patientId);

        return consultations.stream()
                .map(consultationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<ConsultationDTO> getConsultationsByMedecin(Long medecinId) {
        log.info("Récupération des consultations pour le médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        List<Consultation> consultations = consultationRepository.findByMedecinId(medecinId);

        return consultations.stream()
                .map(consultationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<ConsultationDTO> getConsultationsByDate(LocalDate date) {
        log.info("Récupération des consultations pour la date : {}", date);

        List<Consultation> consultations = consultationRepository.findByDateConsultation(date);

        return consultations.stream()
                .map(consultationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
//    @Override
//    public void ajouterNoteConsultation(Long consultationId, String note) {
//        log.info("Ajout d'une note à la consultation avec l'ID : {}", consultationId);
//
//        Consultation consultation = consultationRepository.findById(consultationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId));
//
//        // Ajouter la nouvelle note aux notes existantes ou initialiser si null
//        String notesActuelles = consultation.getNotes();
//        if (notesActuelles == null || notesActuelles.isEmpty()) {
//            consultation.setNotes(note);
//        } else {
//            consultation.setNotes(notesActuelles + "\n" + note);
//        }
//
//        consultationRepository.save(consultation);
//
//        log.info("Note ajoutée avec succès à la consultation");
//    }


    // ✅ CORRECTION : Méthode ajouterNoteConsultation
    @Override
    @Transactional
    public void ajouterNoteConsultation(Long consultationId, String note) {
        log.info("Ajout d'une note à la consultation avec l'ID : {}", consultationId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId));

        // ✅ CORRECTION : Ajouter la nouvelle note aux notes existantes
        String notesActuelles = consultation.getNotes();
        if (notesActuelles == null || notesActuelles.trim().isEmpty()) {
            consultation.setNotes(note);
        } else {
            consultation.setNotes(notesActuelles + ", " + note);
        }

        consultationRepository.save(consultation);

        log.info("Note ajoutée avec succès à la consultation");
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
//    @Override
//    public void ajouterDiagnostic(Long consultationId, String diagnostic) {
//        log.info("Ajout d'un diagnostic à la consultation avec l'ID : {}", consultationId);
//
//        Consultation consultation = consultationRepository.findById(consultationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId));
//
//        consultation.setDiagnostic(diagnostic);
//
//        consultationRepository.save(consultation);
//
//        log.info("Diagnostic ajouté avec succès à la consultation");
//    }


    // ✅ CORRECTION : Méthode ajouterDiagnostic
    @Override
    @Transactional
    public void ajouterDiagnostic(Long consultationId, String diagnostic) {
        log.info("Ajout/Mise à jour du diagnostic pour la consultation avec l'ID : {}", consultationId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId));

        // ✅ CORRECTION : Mise à jour ou ajout du diagnostic
        String diagnosticActuel = consultation.getDiagnostic();
        if (diagnosticActuel == null || diagnosticActuel.trim().isEmpty()) {
            consultation.setDiagnostic(diagnostic);
        } else {
            consultation.setDiagnostic(diagnosticActuel + ", " + diagnostic);
        }

        consultationRepository.save(consultation);

        log.info("Diagnostic ajouté/mis à jour avec succès pour la consultation");
    }


    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PrescriptionDTO> getPrescriptionsByConsultation(Long consultationId) {
        log.info("Récupération des prescriptions pour la consultation avec l'ID : {}", consultationId);

        // Vérifier que la consultation existe
        if (!consultationRepository.existsById(consultationId)) {
            throw new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId);
        }

        List<Prescription> prescriptions = prescriptionRepository.findByConsultationId(consultationId);

        return prescriptions.stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public int countConsultationsByMedecin(Long medecinId) {
        log.info("Comptage des consultations pour le médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        return consultationRepository.countByMedecinId(medecinId);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public int countConsultationsByPatient(Long patientId) {
        log.info("Comptage des consultations pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        return consultationRepository.countByPatientId(patientId);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<ConsultationSummaryDTO> getConsultationsSummaryByPatient(Long patientId) {
        log.info("Récupération des résumés de consultations pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        List<Consultation> consultations = consultationRepository.findByDossierMedicalPatientId(patientId);

        return consultations.stream()
                .map(consultationMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }
}
