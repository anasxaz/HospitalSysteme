package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.DossierMedical;
import com.hospital.HospitalSysteme.entity.Facture;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.entity.RendezVous;
import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import com.hospital.HospitalSysteme.entity.enums.ProfilUser;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.*;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    // Dependency Injection
    private PatientRepository patientRepository;
    private DossierMedicalRepository dossierMedicalRepository;
    private RendezVousRepository rendezVousRepository;
    private ConsultationRepository consultationRepository;
    private PrescriptionRepository prescriptionRepository;
    private PlanDeSoinsRepository planDeSoinsRepository;
    private FactureRepository factureRepository;

    private PatientMapper patientMapper;
    private DossierMedicalMapper dossierMedicalMapper;
    private RendezVousMapper rendezVousMapper;
    private ConsultationMapper consultationMapper;
    private PrescriptionMapper prescriptionMapper;
    private PlanDeSoinsMapper planDeSoinsMapper;
    private FactureMapper factureMapper;
    private PasswordEncoder passwordEncoder;




    @Override
    public PatientDTO createPatient(PatientCreationDTO patientCreationDTO) {
        log.info("Création d'un nouveau patient avec l'email : {}", patientCreationDTO.getEmail());

        // Vérifier si l'email existe déjà
        if (patientRepository.existsByEmail(patientCreationDTO.getEmail())) {
            throw new IllegalArgumentException("Un patient avec cet email existe déjà : " + patientCreationDTO.getEmail());
        }

        // Convert patientCreationDTO to patient JPA Entity
        Patient patient = patientMapper.toEntity(patientCreationDTO);

        // Encoder le mot de passe
        patient.setPassword(passwordEncoder.encode(patientCreationDTO.getPassword()));

        // Définir explicitement le profil
        patient.setProfil(ProfilUser.PATIENT);

        // Créer un dossier médical vide si nécessaire
        if (patient.getDossierMedical() == null) {
            DossierMedical dossierMedical = new DossierMedical();
            dossierMedical.setPatient(patient);
            patient.setDossierMedical(dossierMedical);
        }

        // patient JPA Entity
        Patient savedPatient = patientRepository.save(patient);

        log.info("Patient créé avec succès avec l'ID : {}", savedPatient.getId());

        // Convert saved patient JPA Entity into patientDTO object
        return patientMapper.toDTO(savedPatient);
    }

    @Override
    public PatientDTO getPatientById(Long patientId) {

        log.info("Récupération du patient avec l'ID : {}", patientId);

        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId)
        );

        return patientMapper.toDTO(patient);
    }

    @Override
    public List<PatientDTO> getAllPatients() {

        log.info("Récupération de tous les patients");

        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map((patient) -> patientMapper.toDTO(patient))
                .collect(Collectors.toList());

    }

    @Override
    public PatientDetailDTO getPatientDetailsById(Long patientId) {

        log.info("Récupération des détails du patient avec l'ID : {}", patientId);

        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId)
        );

        return patientMapper.toDetailDTO(patient);

    }

    @Override
    public PatientDTO updatePatient(Long patientId, PatientUpdateDTO patientUpdateDTO) {

        log.info("Mise à jour du patient avec l'ID : {}", patientId);

        // Retrieve the patient
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId)
        );

        // Mettre à jour les infos
        patientMapper.updatePatientFromDTO(patientUpdateDTO, patient);

        // Sauvegarder l'update
        Patient patientUpdated = patientRepository.save(patient);

        log.info("Patient mis à jour avec succès");

        return patientMapper.toDTO(patientUpdated);
    }

    @Override
    public void deletePatient(Long patientId) {

        log.info("Suppression du patient avec l'ID : {}", patientId);

        if (!patientRepository.existsById(patientId)){
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        patientRepository.deleteById(patientId);

        log.info("Patient supprimé avec succès");

    }

    @Override
    public DossierMedicalDTO getPatientDossierMedical(Long patientId) {

        log.info("Récupération du dossier médical pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        DossierMedical dossierMedical = dossierMedicalRepository.findByPatientId(patientId).orElseThrow(
                        () -> new ResourceNotFoundException("Dossier médical non trouvé pour le patient avec l'ID : " + patientId)
                );

        return dossierMedicalMapper.toDTO(dossierMedical);
    }

    // à revoir et vérifier!!
    // De la part de Claude pour s'inspirer
    @Override
    public PatientMedicalSummaryDTO getPatientMedicalSummary(Long patientId) {
        // Vérifier que le patient existe
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId)
        );

        // Récupérer le dossier médical du patient
        DossierMedical dossierMedical = dossierMedicalRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé pour le patient avec l'ID : " + patientId));

        // Créer et remplir le DTO de résumé médical
        PatientMedicalSummaryDTO summaryDTO = new PatientMedicalSummaryDTO();
        summaryDTO.setPatientId(patientId);
        summaryDTO.setPatientNom(patient.getNom());
        summaryDTO.setPatientPrenom(patient.getPrenom());
        summaryDTO.setDateNaissance(patient.getDateNaissance());
        summaryDTO.setGroupeSanguin(patient.getGroupeSanguin());
        summaryDTO.setAllergies(dossierMedical.getPatient().getAllergies());
        summaryDTO.setAntecedentsMedicaux(dossierMedical.getPatient().getAntecedentsMedicaux());

        // Récupérer les dernières consultations (par exemple, les 5 dernières)
        List<ConsultationDTO> dernieresConsultations = consultationRepository.findByDossierMedicalIdOrderByDateDesc(dossierMedical.getId())
                .stream()
                .limit(5)
                .map(consultationMapper::toDTO)
                .collect(Collectors.toList());
        summaryDTO.setDernieresConsultations(dernieresConsultations);

        // Récupérer les prescriptions actives
        LocalDate today = LocalDate.now();
        List<PrescriptionDTO> prescriptionsActives = prescriptionRepository.findByDossierMedicalIdAndDateFinAfter(dossierMedical.getId(), today)
                .stream()
                .map(prescriptionMapper::toDTO)
//                .map((prescription) -> prescriptionMapper.toDTO(prescription))
                .collect(Collectors.toList());
        summaryDTO.setPrescriptionActives(prescriptionsActives);

        // Récupérer les plans de soins actifs
        List<PlanDeSoinsDTO> planDeSoinsActifs = planDeSoinsRepository.findByDossierMedicalIdAndDateFinAfter(dossierMedical.getId(), today)
                .stream()
                .map(planDeSoinsMapper::toDTO)
                .collect(Collectors.toList());
        summaryDTO.setPlanDeSoinsActifs(planDeSoinsActifs);

        return summaryDTO;
    }


    // Par Claude pour s'inspirer
    @Override
    public List<RendezVousDTO> getPatientRendezVous(Long patientId) {
        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        // Récupérer tous les rendez-vous du patient
        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientId(patientId);

        // Convertir les entités en DTOs
        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<RendezVousDTO> getPatientRendezVousAVenir(Long patientId) {

        log.info("Récupération des rendez-vous à venir pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        // Récupérer tous les rendez-vous du patient
        LocalDateTime now = LocalDateTime.now();
//        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientId(patientId);
        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientIdAndDateHeureAfter(patientId, now);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RendezVousDTO> getPatientRendezVousPasses(Long patientId) {

        log.info("Récupération des rendez-vous passés pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        // Récupérer tous les rendez-vous du patient
        LocalDateTime now = LocalDateTime.now();
        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientIdAndDateHeureBefore(patientId, now);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RendezVousDTO> getPatientRendezVousByMedecin(Long patientId, Long medecinId) {

        log.info("Récupération des rendez-vous du médecin d'ID : {} pour le patient avec l'ID : {}",medecinId, patientId);

        // Vérifier l'existence du patient
        if(!patientRepository.existsById(patientId)) { // Correction ici
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        // Retrieve tous les rendez-vous du patient et médecin précis
        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientIdAndMedecinId(patientId, medecinId);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getPatientFactures(Long patientId) {

        log.info("Récupération des factures pour le patient avec l'ID : {}", patientId);

        // Vérifier l'existence du patient
        if(!patientRepository.existsById(patientId)) { // Correction ici
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        // Retrieve toutes les factures du patient
        List<Facture> factures = factureRepository.findByPatientId(patientId);


        return factures.stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getPatientFacturesByStatut(Long patientId, StatutPaiement statutPaiement) {

        log.info("Récupération des factures pour le patient avec l'ID : {} avec le statut de paiement : {}", patientId, statutPaiement);

        // Vérifier l'existence du patient
        if(!patientRepository.existsById(patientId)) { // Correction ici
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        // Retrieve toutes les factures du patient avec un statut de paiement (PAYEE, EN_ATTENTE, ANNULEE)
        List<Facture> factures = factureRepository.findByPatientIdAndStatutPaiement(patientId, statutPaiement);

        return factures.stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // Par Claude pour s'inspirer
    @Override
    public List<PatientDTO> searchPatients(String query) {

        log.info("Récupération le patient avec la query : {}", query);

        // Recherche par nom, prénom, email ou numéro d'assurance
        String searchTerm = "%" + query.toLowerCase() + "%";

        List<Patient> patients = patientRepository.searchPatients(searchTerm);

        return patients.stream()
                .map(patientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientDTO> getPatientsByGroupeSanguin(GroupeSanguin groupeSanguin) {

        log.info("Récupération les patients avec le groupe sanguin : {}", groupeSanguin);

        // Retrieve tous les patients avec ce groupe sanguin
        List<Patient> patients = patientRepository.findByGroupeSanguin(groupeSanguin);

        return patients.stream()
                .map(patientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientDTO> getPatientsByAllergies(String allergies) {

        log.info("Récupération les patients avec les allergies : {}", allergies);

        // Retrieve tous les patients avec l'allergies
        List<Patient> patients = patientRepository.findByAllergieContaining(allergies);

        return patients.stream()
                .map(patientMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // Par Claude pour s'inspirer
    @Override
    public PatientDTO getPatientByNumeroAssurance(String numeroAssurance) {

        log.info("Récupération du patient avec le numéro d'assurance : {}", numeroAssurance);

        // Retrieve tous les patients avec le numéro d'assurance
        Patient patient = patientRepository.findByNumeroAssurance(numeroAssurance)
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec le numéro d'assurance : " + numeroAssurance));

        return patientMapper.toDTO(patient);
    }


//    @Override
//    public PatientDTO getPatientByNumeroAssurance(String numeroAssurance) {
//        log.info("Récupération du patient avec le numéro d'assurance : {}", numeroAssurance);
//
//        List<Patient> patients = patientRepository.findByNumeroAssurance(numeroAssurance);
//
//        if (patients.isEmpty()) {
//            throw new ResourceNotFoundException("Patient non trouvé avec le numéro d'assurance : " + numeroAssurance);
//        }
//
//        // Prendre le premier patient trouvé (normalement il devrait être unique)
//        Patient patient = patients.get(0);
//
//        return patientMapper.toDTO(patient);
//    }

}
