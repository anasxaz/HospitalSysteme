package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.Consultation;
import com.hospital.HospitalSysteme.entity.Prescription;
import com.hospital.HospitalSysteme.service.ConsultationService;

import java.time.LocalDate;
import java.util.List;

import com.hospital.HospitalSysteme.dto.RendezVousCreationDTO;
import com.hospital.HospitalSysteme.dto.RendezVousDTO;
import com.hospital.HospitalSysteme.dto.RendezVousUpdateDTO;
import com.hospital.HospitalSysteme.entity.Medecin;
import com.hospital.HospitalSysteme.entity.RendezVous;
import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.ConsultationMapper;
import com.hospital.HospitalSysteme.mapper.MedecinMapper;
import com.hospital.HospitalSysteme.mapper.PrescriptionMapper;
import com.hospital.HospitalSysteme.mapper.RendezVousMapper;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.RendezVousService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private MedecinMapper medecinMapper;
    private RendezVousMapper rendezVousMapper;
    private ConsultationMapper consultationMapper;
    private PrescriptionMapper prescriptionMapper;




    @Override
    public ConsultationDTO createConsultation(ConsultationCreationDTO consultationCreationDTO) {
        log.info("Création d'une nouvelle consultation le : {}", consultationCreationDTO.getDate());

        // Convert consultationCreationDTO to consultation JPA Entity
        Consultation consultation = consultationMapper.toEntity(consultationCreationDTO);

        // consultation JPA Entity
        Consultation savedConsultation = consultationRepository.save(consultation);

        log.info("Consultation créée avec succès avec l'ID : {}", savedConsultation.getId());

        // Convert saved consultation JPA Entity into DTO object
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
    @Override
    public void ajouterNoteConsultation(Long consultationId, String note) {
        log.info("Ajout d'une note à la consultation avec l'ID : {}", consultationId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId));

        // Ajouter la nouvelle note aux notes existantes ou initialiser si null
        String notesActuelles = consultation.getNotes();
        if (notesActuelles == null || notesActuelles.isEmpty()) {
            consultation.setNotes(note);
        } else {
            consultation.setNotes(notesActuelles + "\n" + note);
        }

        consultationRepository.save(consultation);

        log.info("Note ajoutée avec succès à la consultation");
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public void ajouterDiagnostic(Long consultationId, String diagnostic) {
        log.info("Ajout d'un diagnostic à la consultation avec l'ID : {}", consultationId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation non trouvée avec l'ID : " + consultationId));

        consultation.setDiagnostic(diagnostic);

        consultationRepository.save(consultation);

        log.info("Diagnostic ajouté avec succès à la consultation");
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
