package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.ConsultationDTO;
import com.hospital.HospitalSysteme.dto.DossierMedicalCreationDTO;
import com.hospital.HospitalSysteme.dto.DossierMedicalDTO;
import com.hospital.HospitalSysteme.dto.DossierMedicalDetailDTO;
import com.hospital.HospitalSysteme.dto.PrescriptionDTO;
import com.hospital.HospitalSysteme.entity.Consultation;
import com.hospital.HospitalSysteme.entity.DossierMedical;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.entity.Prescription;
import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.ConsultationMapper;
import com.hospital.HospitalSysteme.mapper.DossierMedicalMapper;
import com.hospital.HospitalSysteme.mapper.PrescriptionMapper;
import com.hospital.HospitalSysteme.repository.ConsultationRepository;
import com.hospital.HospitalSysteme.repository.DossierMedicalRepository;
import com.hospital.HospitalSysteme.repository.PatientRepository;
import com.hospital.HospitalSysteme.repository.PrescriptionRepository;
import com.hospital.HospitalSysteme.service.DossierMedicalService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DossierMedicalServiceImpl implements DossierMedicalService {

    private DossierMedicalRepository dossierMedicalRepository;
    private PatientRepository patientRepository;
    private ConsultationRepository consultationRepository;
    private PrescriptionRepository prescriptionRepository;
    private DossierMedicalMapper dossierMedicalMapper;
    private ConsultationMapper consultationMapper;
    private PrescriptionMapper prescriptionMapper;

//    @Override
//    public DossierMedicalDTO createDossierMedical(DossierMedicalCreationDTO dossierMedicalCreationDTO) {
//        log.info("Création d'un nouveau dossier médical pour le patient avec l'ID : {}", dossierMedicalCreationDTO.getPatientId());
//
//        // Vérifier si le patient existe
//        Patient patient = patientRepository.findById(dossierMedicalCreationDTO.getPatientId())
//                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + dossierMedicalCreationDTO.getPatientId()));
//
//        // Vérifier si le patient a déjà un dossier médical
//        if (dossierMedicalRepository.existsByPatientId(dossierMedicalCreationDTO.getPatientId())) {
//            throw new IllegalStateException("Le patient a déjà un dossier médical");
//        }
//
//        DossierMedical dossierMedical = new DossierMedical();
//        dossierMedical.setPatient(patient);
//        dossierMedical.setDateCreation(LocalDateTime.now());
//        dossierMedical.setAntecedentsMedicaux(dossierMedicalCreationDTO.getAntecedentsMedicaux());
//        dossierMedical.setAllergies(dossierMedicalCreationDTO.getAllergies());
//        // Si le groupe sanguin est disponible dans le patient, on peut le récupérer
//        if (patient.getGroupeSanguin() != null) {
//            dossierMedical.setGroupeSanguin(patient.getGroupeSanguin());
//        }
//
//        DossierMedical savedDossierMedical = dossierMedicalRepository.save(dossierMedical);
//        log.info("Dossier médical créé avec succès avec l'ID : {}", savedDossierMedical.getId());
//
//        return dossierMedicalMapper.toDTO(savedDossierMedical);
//    }



    // 2ème tentative :
//    @Override
//    public DossierMedicalDTO createDossierMedical(DossierMedicalCreationDTO dossierMedicalCreationDTO) {
//        log.info("Création/mise à jour d'un dossier médical pour le patient avec l'ID : {}", dossierMedicalCreationDTO.getPatientId());
//
//        Patient patient = patientRepository.findById(dossierMedicalCreationDTO.getPatientId())
//                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + dossierMedicalCreationDTO.getPatientId()));
//
//        // Vérifier si le patient a déjà un dossier médical
//        Optional<DossierMedical> existingDossier = dossierMedicalRepository.findByPatientId(dossierMedicalCreationDTO.getPatientId());
//
//        DossierMedical dossierMedical;
//
//        if (existingDossier.isPresent()) {
//            // Si le dossier existe, le mettre à jour
//            dossierMedical = existingDossier.get();
//            log.info("Mise à jour du dossier médical existant avec l'ID : {}", dossierMedical.getId());
//
//            // Mettre à jour seulement les champs fournis (non null)
//            if (dossierMedicalCreationDTO.getAntecedentsMedicaux() != null) {
//                dossierMedical.setAntecedentsMedicaux(dossierMedicalCreationDTO.getAntecedentsMedicaux());
//            }
//            if (dossierMedicalCreationDTO.getAllergies() != null) {
//                dossierMedical.setAllergies(dossierMedicalCreationDTO.getAllergies());
//            }
//
//            // @UpdateTimestamp se charge automatiquement de dateMiseAJour
//        } else {
//            // Si aucun dossier n'existe, en créer un nouveau
//            dossierMedical = dossierMedicalMapper.toEntity(dossierMedicalCreationDTO);
//            dossierMedical.setPatient(patient);
//            // @CreationTimestamp et @UpdateTimestamp se chargent automatiquement des dates
//            log.info("Création d'un nouveau dossier médical");
//        }
//
//        DossierMedical savedDossierMedical = dossierMedicalRepository.save(dossierMedical);
//
//        log.info("Dossier médical créé/mis à jour avec succès avec l'ID : {}", savedDossierMedical.getId());
//
//        return dossierMedicalMapper.toDTO(savedDossierMedical);
//    }


    // 3ème tentative :
//    @Override
//    public DossierMedicalDTO createDossierMedical(DossierMedicalCreationDTO dossierMedicalCreationDTO) {
//        log.info("Création/mise à jour d'un dossier médical pour le patient avec l'ID : {}", dossierMedicalCreationDTO.getPatientId());
//
//        Patient patient = patientRepository.findById(dossierMedicalCreationDTO.getPatientId())
//                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + dossierMedicalCreationDTO.getPatientId()));
//
//        // Vérifier si le patient a déjà un dossier médical
//        Optional<DossierMedical> existingDossier = dossierMedicalRepository.findByPatientId(dossierMedicalCreationDTO.getPatientId());
//
//        DossierMedical dossierMedical;
//
//        if (existingDossier.isPresent()) {
//            // Si le dossier existe, le mettre à jour
//            dossierMedical = existingDossier.get();
//            log.info("Mise à jour du dossier médical existant avec l'ID : {}", dossierMedical.getId());
//
//            // Utiliser la nouvelle méthode du mapper pour la mise à jour
//            dossierMedicalMapper.updateDossierMedicalFromCreationDTO(dossierMedicalCreationDTO, dossierMedical);
//
//            // @UpdateTimestamp se charge automatiquement de dateMiseAJour
//        } else {
//            // Si aucun dossier n'existe, en créer un nouveau
//            dossierMedical = dossierMedicalMapper.toEntity(dossierMedicalCreationDTO);
//            dossierMedical.setPatient(patient);
//            // @CreationTimestamp et @UpdateTimestamp se chargent automatiquement des dates
//            log.info("Création d'un nouveau dossier médical");
//        }
//
//        DossierMedical savedDossierMedical = dossierMedicalRepository.save(dossierMedical);
//
//        log.info("Dossier médical créé/mis à jour avec succès avec l'ID : {}", savedDossierMedical.getId());
//
//        return dossierMedicalMapper.toDTO(savedDossierMedical);
//    }

    @Override
    public DossierMedicalDTO createDossierMedical(DossierMedicalCreationDTO dossierMedicalCreationDTO) {
        log.info("Création/mise à jour d'un dossier médical pour le patient avec l'ID : {}", dossierMedicalCreationDTO.getPatientId());

        Patient patient = patientRepository.findById(dossierMedicalCreationDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + dossierMedicalCreationDTO.getPatientId()));

        // Vérifier si le patient a déjà un dossier médical
        Optional<DossierMedical> existingDossier = dossierMedicalRepository.findByPatientId(dossierMedicalCreationDTO.getPatientId());

        DossierMedical dossierMedical;

        if (existingDossier.isPresent()) {
            // Si le dossier existe, le mettre à jour
            dossierMedical = existingDossier.get();
            log.info("Mise à jour du dossier médical existant avec l'ID : {}", dossierMedical.getId());

            // Utiliser la nouvelle méthode du mapper pour la mise à jour
            dossierMedicalMapper.updateDossierMedicalFromCreationDTO(dossierMedicalCreationDTO, dossierMedical);

            // ✅ RÉCUPÉRER LE GROUPE SANGUIN DU PATIENT SI PAS DÉJÀ DÉFINI
            if (dossierMedical.getGroupeSanguin() == null && patient.getGroupeSanguin() != null) {
                dossierMedical.setGroupeSanguin(patient.getGroupeSanguin());
            }

            // @UpdateTimestamp se charge automatiquement de dateMiseAJour
        } else {
            // Si aucun dossier n'existe, en créer un nouveau
            dossierMedical = dossierMedicalMapper.toEntity(dossierMedicalCreationDTO);
            dossierMedical.setPatient(patient);

            // ✅ RÉCUPÉRER AUTOMATIQUEMENT LE GROUPE SANGUIN DU PATIENT
            if (patient.getGroupeSanguin() != null) {
                dossierMedical.setGroupeSanguin(patient.getGroupeSanguin());
            }

            // @CreationTimestamp et @UpdateTimestamp se chargent automatiquement des dates
            log.info("Création d'un nouveau dossier médical");
        }

        DossierMedical savedDossierMedical = dossierMedicalRepository.save(dossierMedical);

        log.info("Dossier médical créé/mis à jour avec succès avec l'ID : {}", savedDossierMedical.getId());

        return dossierMedicalMapper.toDTO(savedDossierMedical);
    }

    @Override
    public DossierMedicalDTO getDossierMedicalById(Long dossierMedicalId) {
        log.info("Récupération du dossier médical avec l'ID : {}", dossierMedicalId);

        DossierMedical dossierMedical = dossierMedicalRepository.findById(dossierMedicalId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId));

        return dossierMedicalMapper.toDTO(dossierMedical);
    }

    @Override
    public DossierMedicalDetailDTO getDossierMedicalDetailById(Long dossierMedicalId) {
        log.info("Récupération des détails du dossier médical avec l'ID : {}", dossierMedicalId);

        DossierMedical dossierMedical = dossierMedicalRepository.findById(dossierMedicalId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId));

        return dossierMedicalMapper.toDetailDTO(dossierMedical);
    }

    @Override
    public DossierMedicalDTO updateDossierMedical(Long dossierMedicalId, DossierMedicalCreationDTO dossierMedicalUpdateDTO) {
        log.info("Mise à jour du dossier médical avec l'ID : {}", dossierMedicalId);

        DossierMedical dossierMedical = dossierMedicalRepository.findById(dossierMedicalId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId));

        // Mise à jour des champs modifiables
        if (dossierMedicalUpdateDTO.getAntecedentsMedicaux() != null) {
            dossierMedical.setAntecedentsMedicaux(dossierMedicalUpdateDTO.getAntecedentsMedicaux());
        }

        if (dossierMedicalUpdateDTO.getAllergies() != null) {
            dossierMedical.setAllergies(dossierMedicalUpdateDTO.getAllergies());
        }

        // Si l'ID du patient est modifié, vérifier que le nouveau patient existe
        if (dossierMedicalUpdateDTO.getPatientId() != null &&
                !dossierMedicalUpdateDTO.getPatientId().equals(dossierMedical.getPatient().getId())) {

            Patient newPatient = patientRepository.findById(dossierMedicalUpdateDTO.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + dossierMedicalUpdateDTO.getPatientId()));

            // Vérifier si le nouveau patient a déjà un dossier médical
            if (dossierMedicalRepository.existsByPatientId(dossierMedicalUpdateDTO.getPatientId())) {
                log.warn("Le patient avec l'ID {} a déjà un dossier médical", dossierMedicalUpdateDTO.getPatientId());
                throw new IllegalStateException("Le nouveau patient a déjà un dossier médical");
            }

            dossierMedical.setPatient(newPatient);

            // Mettre à jour le groupe sanguin si disponible dans le patient
            if (newPatient.getGroupeSanguin() != null) {
                dossierMedical.setGroupeSanguin(newPatient.getGroupeSanguin());
            }
        }

        DossierMedical updatedDossierMedical = dossierMedicalRepository.save(dossierMedical);
        log.info("Dossier médical mis à jour avec succès");

        return dossierMedicalMapper.toDTO(updatedDossierMedical);
    }

//    @Override
//    public void deleteDossierMedical(Long dossierMedicalId) {
//        log.info("Suppression du dossier médical avec l'ID : {}", dossierMedicalId);
//
//        if (!dossierMedicalRepository.existsById(dossierMedicalId)) {
//            throw new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId);
//        }
//
//        dossierMedicalRepository.deleteById(dossierMedicalId);
//        log.info("Dossier médical supprimé avec succès");
//    }

    //2ème tentative
//    @Override
//    @Transactional  // ✅ Ajoutez cette annotation !
//    public void deleteDossierMedical(Long dossierMedicalId) {
//    log.info("Suppression du dossier médical avec l'ID : {}", dossierMedicalId);
//
//    // Debug 1 : Vérifier l'existence
//    boolean exists = dossierMedicalRepository.existsById(dossierMedicalId);
//    log.info("Le dossier médical avec l'ID {} existe : {}", dossierMedicalId, exists);
//
//    if (!exists) {
//        throw new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId);
//    }
//
//    // Debug 2 : Récupérer l'entité pour voir ses relations
//    Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierMedicalId);
//    if (dossierOpt.isPresent()) {
//        DossierMedical dossier = dossierOpt.get();
//        log.info("Dossier trouvé - Patient ID: {}, Nombre de consultations: {}",
//                dossier.getPatient().getId(),
//                dossier.getConsultations() != null ? dossier.getConsultations().size() : 0);
//    }
//
//    // Debug 3 : Tentative de suppression
//    log.info("Tentative de suppression du dossier médical avec l'ID : {}", dossierMedicalId);
//    dossierMedicalRepository.deleteById(dossierMedicalId);
//
//    // Debug 4 : Vérifier que la suppression a eu lieu
//    boolean stillExists = dossierMedicalRepository.existsById(dossierMedicalId);
//    log.info("Après suppression, le dossier existe encore : {}", stillExists);
//
//    if (stillExists) {
//        log.error("ERREUR : Le dossier n'a pas été supprimé !");
//        throw new RuntimeException("Échec de la suppression du dossier médical");
//    }
//
//    log.info("Dossier médical supprimé avec succès");
//}


//    @Override
//    @Transactional
//    public void deleteDossierMedical(Long dossierMedicalId) {
//        log.info("Suppression du dossier médical avec l'ID : {}", dossierMedicalId);
//
//        if (!dossierMedicalRepository.existsById(dossierMedicalId)) {
//            throw new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId);
//        }
//
//        // La suppression en cascade se fera automatiquement
//        dossierMedicalRepository.deleteById(dossierMedicalId);
//        log.info("Dossier médical supprimé avec succès");
//    }



    @Override
    @Transactional
    public void deleteDossierMedical(Long dossierMedicalId) {
        log.info("Suppression du dossier médical avec l'ID : {}", dossierMedicalId);

        DossierMedical dossierMedical = dossierMedicalRepository.findById(dossierMedicalId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId));

        Patient patient = dossierMedical.getPatient();
        log.info("Dossier trouvé pour le patient : {} {}", patient.getNom(), patient.getPrenom());

        // ✅ ÉTAPE 1 : Supprimer les consultations associées (cascade)
        if (!dossierMedical.getConsultations().isEmpty()) {
            log.info("Suppression de {} consultations associées", dossierMedical.getConsultations().size());
            dossierMedical.getConsultations().clear(); // Le cascade s'occupera de la suppression
        }

        // ✅ ÉTAPE 2 : Détacher le dossier médical du patient
        // Option A : Mettre la référence à null dans le patient
        if (patient.getDossierMedical() != null && patient.getDossierMedical().getId().equals(dossierMedicalId)) {
            patient.setDossierMedical(null);
            patientRepository.save(patient);
            log.info("Référence du dossier médical supprimée du patient");
        }

        // ✅ ÉTAPE 3 : Détacher le patient du dossier médical
        dossierMedical.setPatient(null);
        dossierMedicalRepository.save(dossierMedical);

        // ✅ ÉTAPE 4 : Maintenant on peut supprimer le dossier médical
        dossierMedicalRepository.delete(dossierMedical);

        log.info("Dossier médical supprimé avec succès");
    }

    @Override
    public DossierMedicalDTO getDossierMedicalByPatientId(Long patientId) {
        log.info("Récupération du dossier médical pour le patient avec l'ID : {}", patientId);

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        DossierMedical dossierMedical = dossierMedicalRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier médical non trouvé pour le patient avec l'ID : " + patientId));

        return dossierMedicalMapper.toDTO(dossierMedical);
    }

    @Override
    public List<ConsultationDTO> getConsultationsByDossierMedicalId(Long dossierMedicalId) {
        log.info("Récupération des consultations pour le dossier médical avec l'ID : {}", dossierMedicalId);

        if (!dossierMedicalRepository.existsById(dossierMedicalId)) {
            throw new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId);
        }

        List<Consultation> consultations = consultationRepository.findByDossierMedicalId(dossierMedicalId);

        return consultations.stream()
                .map(consultationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByDossierMedicalId(Long dossierMedicalId) {
        log.info("Récupération des prescriptions pour le dossier médical avec l'ID : {}", dossierMedicalId);

        if (!dossierMedicalRepository.existsById(dossierMedicalId)) {
            throw new ResourceNotFoundException("Dossier médical non trouvé avec l'ID : " + dossierMedicalId);
        }

        List<Prescription> prescriptions = prescriptionRepository.findByConsultationDossierMedicalId(dossierMedicalId);

        return prescriptions.stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int countDossiersMedicaux() {
        log.info("Comptage du nombre total de dossiers médicaux");
        return (int) dossierMedicalRepository.count();
    }

    @Override
    public List<DossierMedicalDTO> searchDossiersMedicauxByPatientNom(String nom) {
        log.info("Recherche de dossiers médicaux par nom de patient : {}", nom);

        List<DossierMedical> dossiersMedicaux = dossierMedicalRepository.findByPatientNomContainingIgnoreCase(nom);

        return dossiersMedicaux.stream()
                .map(dossierMedicalMapper::toDTO)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<DossierMedicalDTO> getDossiersMedicauxByGroupeSanguin(GroupeSanguin groupeSanguin) {
//        log.info("Récupération des dossiers médicaux par groupe sanguin : {}", groupeSanguin);
//
//        // ✅ Maintenant on passe l'enum directement
//        List<DossierMedical> dossiersMedicaux = dossierMedicalRepository.findByGroupeSanguin(String.valueOf(groupeSanguin));
//
//        return dossiersMedicaux.stream()
//                .map(dossierMedicalMapper::toDTO)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<DossierMedicalDTO> getDossiersMedicauxByGroupeSanguin(GroupeSanguin groupeSanguin) {
        log.info("Récupération des dossiers médicaux par groupe sanguin : {}", groupeSanguin);

        // ✅ CORRIGÉ : on utilise la bonne méthode du repository
        List<DossierMedical> dossiersMedicaux = dossierMedicalRepository.findByPatientGroupeSanguin(groupeSanguin);

        return dossiersMedicaux.stream()
                .map(dossierMedicalMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DossierMedicalDTO> getDossiersMedicauxWithAllergies(String allergies) {
        log.info("Récupération des dossiers médicaux contenant les allergies : {}", allergies);

        List<DossierMedical> dossiersMedicaux = dossierMedicalRepository.findByAllergiesContainingIgnoreCase(allergies);

        return dossiersMedicaux.stream()
                .map(dossierMedicalMapper::toDTO)
                .collect(Collectors.toList());
    }
}