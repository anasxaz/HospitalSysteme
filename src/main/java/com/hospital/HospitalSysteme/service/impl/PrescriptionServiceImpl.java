package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.Consultation;
import com.hospital.HospitalSysteme.entity.Medicament;
import com.hospital.HospitalSysteme.entity.Prescription;
import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.*;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.PrescriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {

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
    private MedicamentMapper medicamentMapper;



    @Override
    public PrescriptionDTO createPrescription(PrescriptionCreationDTO prescriptionCreationDTO) {
        log.info("Création d'une nouvelle prescription le : {}", prescriptionCreationDTO.getDateDebut());

        // Convert prescriptionCreationDTO to prescription JPA Entity
        Prescription prescription = prescriptionMapper.toEntity(prescriptionCreationDTO);

        // prescription JPA Entity
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        log.info("Prescription créée avec succès avec l'ID : {}", savedPrescription.getId());

        // Convert savedPrescription JPA Entity into DTO object
        return prescriptionMapper.toDTO(savedPrescription);
    }

    @Override
    public PrescriptionDTO getPrescriptionById(Long prescriptionId) {
        log.info("Récupération de la prescription avec l'ID : {}", prescriptionId);

        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElseThrow(
                () -> new ResourceNotFoundException("Consultation non trouvé avec l'ID : " + prescriptionId)
        );

        return prescriptionMapper.toDTO(prescription);
    }

    @Override
    public List<PrescriptionDTO> getAllPrescriptions() {
        log.info("Récupération de toutes les prescriptions");

        List<Prescription> prescriptions = prescriptionRepository.findAll();

        return prescriptions.stream()
                .map((prescription) -> prescriptionMapper.toDTO(prescription))
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionDTO updatePrescription(Long prescriptionId, PrescriptionUpdateDTO prescriptionUpdateDTO) {
        log.info("Mise à jour de la prescription avec l'ID : {}", prescriptionId);

        // Récupérer la prescription existante
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription non trouvée avec l'ID : " + prescriptionId));

        // Mettre à jour les informations de la prescription
        prescriptionMapper.updatePrescriptionFromDTO(prescriptionUpdateDTO, prescription);

        // Sauvegarder les modifications
        Prescription updatedPrescription = prescriptionRepository.save(prescription);

        log.info("Prescription mis à jour avec succès");

        // Convertir et retourner la prescription mis à jour
        return prescriptionMapper.toDTO(updatedPrescription);
    }

    @Override
    public void deletePrescription(Long prescriptionId) {
        log.info("Suppression de la prescription avec l'ID : {}", prescriptionId);

        if (!prescriptionRepository.existsById(prescriptionId)){
            throw new ResourceNotFoundException("Prescription non trouvée avec l'ID : " + prescriptionId);
        }

        prescriptionRepository.deleteById(prescriptionId);

        log.info("Prescription supprimée avec succès");

    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PrescriptionDTO> getPrescriptionsByPatient(Long patientId) {
        log.info("Récupération des prescriptions pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientId);

        return prescriptions.stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PrescriptionDTO> getPrescriptionsByMedecin(Long medecinId) {
        log.info("Récupération des prescriptions pour le médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        List<Prescription> prescriptions = prescriptionRepository.findByMedecinId(medecinId);

        return prescriptions.stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
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
    public List<PrescriptionDTO> getPrescriptionsByDate(LocalDate date) {
        log.info("Récupération des prescriptions pour la date : {}", date);

        List<Prescription> prescriptions = prescriptionRepository.findByDateDebut(date);

        return prescriptions.stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public void ajouterMedicamentAPrescription(Long prescriptionId, MedicamentDTO medicamentDTO) {
        log.info("Ajout d'un médicament à la prescription avec l'ID : {}", prescriptionId);

        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription non trouvée avec l'ID : " + prescriptionId));

        // Convertir le DTO en entité
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);

        // Ajouter le médicament à la prescription
        prescription.getMedicaments().add(medicament);
        medicament.getPrescriptions().add(prescription);

        // Sauvegarder les modifications
        prescriptionRepository.save(prescription);

        log.info("Médicament ajouté avec succès à la prescription");
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public void supprimerMedicamentDePrescription(Long prescriptionId, Long medicamentId) {
        log.info("Suppression du médicament avec l'ID : {} de la prescription avec l'ID : {}", medicamentId, prescriptionId);

        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription non trouvée avec l'ID : " + prescriptionId));

        // Trouver et supprimer le médicament de la liste
        prescription.getMedicaments().removeIf(medicament -> medicament.getId().equals(medicamentId));

        // Sauvegarder les modifications
        prescriptionRepository.save(prescription);

        log.info("Médicament supprimé avec succès de la prescription");
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public PrescriptionDTO renouvelerPrescription(Long prescriptionId, int dureeJours) {
        log.info("Renouvellement de la prescription avec l'ID : {} pour {} jours", prescriptionId, dureeJours);

        if (dureeJours <= 0) {
            throw new IllegalArgumentException("La durée de renouvellement doit être positive");
        }

        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription non trouvée avec l'ID : " + prescriptionId));

        // Créer une nouvelle prescription basée sur l'ancienne
        Prescription nouvellePrescription = new Prescription();
        nouvellePrescription.setDate(LocalDate.now());
        nouvellePrescription.setInstructions(prescription.getInstructions());
        nouvellePrescription.setStatut(StatutPrescription.ACTIVE); // Définir le statut comme ACTIVE pour la nouvelle prescription
        nouvellePrescription.setConsultation(prescription.getConsultation());

        // Copier les médicaments de l'ancienne prescription
        nouvellePrescription.setMedicaments(new HashSet<>(prescription.getMedicaments()));

        // Sauvegarder la nouvelle prescription
        Prescription savedPrescription = prescriptionRepository.save(nouvellePrescription);

        // Mettre à jour les relations bidirectionnelles avec les médicaments si nécessaire
        for (Medicament medicament : savedPrescription.getMedicaments()) {
            medicament.getPrescriptions().add(savedPrescription);
        }

        log.info("Prescription renouvelée avec succès avec l'ID : {}", savedPrescription.getId());

        return prescriptionMapper.toDTO(savedPrescription);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public int countPrescriptionsByMedecin(Long medecinId) {
        log.info("Comptage des prescriptions pour le médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        return prescriptionRepository.countByMedecinId(medecinId);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public int countPrescriptionsByPatient(Long patientId) {
        log.info("Comptage des prescriptions pour le patient avec l'ID : {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        return prescriptionRepository.countByPatientId(patientId);
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<MedicamentStatDTO> getMedicamentsLesPlusPrescrits(int limit) {
        log.info("Récupération des {} médicaments les plus prescrits", limit);

        if (limit <= 0) {
            throw new IllegalArgumentException("La limite doit être positive");
        }

        // Cette méthode nécessite une requête personnalisée dans le repository
        List<Object[]> results = prescriptionRepository.findMostPrescribedMedicaments(limit);

        // Transformer les résultats en DTOs
        return results.stream()
                .map(result -> {
                    MedicamentStatDTO dto = new MedicamentStatDTO();
                    dto.setId((Long) result[0]);
                    dto.setNom((String) result[1]);
                    dto.setNombrePrescriptions(((Number) result[2]).intValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
