package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.*;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.*;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.MedecinService;
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
public class MedecinServiceImpl implements MedecinService {

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
    public MedecinDTO createMedecin(MedecinCreationDTO medecinCreationDTO) {

        log.info("Création d'un nouveau médecin avec l'email : {}", medecinCreationDTO.getEmail());

        // Vérifier si l'email existe déjà
        if (medecinRepository.existsByEmail(medecinCreationDTO.getEmail())) {
            throw new IllegalArgumentException("Un médecin avec cet email existe déjà : " + medecinCreationDTO.getEmail());
        }

        // Convert medecinCreationDTO to patient JPA Entity
        Medecin medecin = medecinMapper.toEntity(medecinCreationDTO);

        // patient JPA Entity
        Medecin savedMedecin = medecinRepository.save(medecin);

        log.info("Médecin créé avec succès avec l'ID : {}", savedMedecin.getId());

        // Convert saved medecin JPA Entity into DTO object
        return medecinMapper.toDTO(savedMedecin);
    }

    @Override
    public MedecinDTO getMedecinById(Long medecinId) {
        log.info("Récupération du médecin avec l'ID : {}", medecinId);

        Medecin medecin = medecinRepository.findById(medecinId).orElseThrow(
                () -> new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId)
        );

        return medecinMapper.toDTO(medecin);
    }

    @Override
    public List<MedecinDTO> getAllMedecins() {
        log.info("Récupération de tous les médecins");

        List<Medecin> medecins = medecinRepository.findAll();

        return medecins.stream()
                .map((patient) -> medecinMapper.toDTO(patient))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMedecin(Long medecinId) {

        log.info("Suppression du médecin avec l'ID : {}", medecinId);

        if (!medecinRepository.existsById(medecinId)){
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        medecinRepository.deleteById(medecinId);

        log.info("Médecin supprimé avec succès");

    }

    @Override
    public List<RendezVousDTO> getMedecinRendezVous(Long medecinId) {
        log.info("Récupération des rendez-vous pour le médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        // Récupérer tous les rendez-vous du médecin
        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinId(medecinId);

        // Convertir les entités en DTOs
        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RendezVousDTO> getMedecinRendezVousAVenir(Long medecinId) {
        log.info("Récupération des rendez-vous à venir pour le médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        // Récupérer tous les rendez-vous du patient
        LocalDateTime now = LocalDateTime.now();
//        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientId(patientId);
        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinIdAndDateHeureAfter(medecinId, now);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RendezVousDTO> getMedecinRendezVousPasses(Long medecinId) {
        log.info("Récupération des rendez-vous passés pour le médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        // Récupérer tous les rendez-vous du médecin
        LocalDateTime now = LocalDateTime.now();
        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinIdAndDateHeureBefore(medecinId, now);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<RendezVousDTO> getMedecinRendezVousByDate(Long medecinId, LocalDate date) {
        log.info("Récupération des rendez-vous dans la date {} pour le médecin avec l'ID : {}", date, medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinIdAndDate(medecinId, date);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RendezVousDTO> getMedecinRendezVousByPatient(Long medecinId, Long patientId) {
        log.info("Récupération des rendez-vous du patient d'ID : {} pour le médecin avec l'ID : {}",patientId, medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }
        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        // Retrieve tous les rendez-vous du patient et médecin précis
        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinIdAndPatientId(medecinId, patientId);

        return rendezVousList.stream()
                .map(rendezVousMapper::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<ConsultationDTO> getMedecinConsultations(Long medecinId) {
        log.info("Récupération des consultations du médecin d'ID : {}",medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        List<Consultation> consultations = consultationRepository.findByMedecinId(medecinId);

        return consultations.stream()
                .map(consultationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationDTO> getMedecinConsultationsByPatient(Long medecinId, Long patientId) {
        log.info("Récupération des consultations du médecin d'ID : {} avec le patient d'ID : {}",medecinId, patientId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }
        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        List<Consultation> consultations = consultationRepository.findByMedecinIdAndPatientId(medecinId, patientId);

        return consultations.stream()
                .map(consultationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationDTO> getMedecinConsultationsByPeriode(Long medecinId, LocalDateTime debut, LocalDateTime fin) {

        log.info("Récupération des consultations du médecin d'ID : {} entre {} et {}",medecinId, debut, fin);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        List<Consultation> consultations = consultationRepository.findByMedecinIdAndDateBetween(medecinId, debut, fin);

        return consultations.stream()
                .map(consultationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PrescriptionDTO> getMedecinPrescriptions(Long medecinId) {
        log.info("Récupération des prescriptions du médecin avec l'ID : {}", medecinId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        // Vous devez ajouter cette méthode dans PrescriptionRepository
        List<Prescription> prescriptions = prescriptionRepository.findByMedecinId(medecinId);

        return prescriptions.stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PrescriptionDTO> getMedecinPrescriptionsByPatient(Long medecinId, Long patientId) {
        log.info("Récupération des prescriptions du médecin avec l'ID : {} pour le patient avec l'ID : {}", medecinId, patientId);

        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId);
        }

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        // Vous devez ajouter cette méthode dans PrescriptionRepository
        List<Prescription> prescriptions = prescriptionRepository.findByMedecinIdAndPatientId(medecinId, patientId);

        return prescriptions.stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<MedecinDTO> searchMedecins(String query) {
        log.info("Recherche de médecins avec la requête : {}", query);

        // Recherche par nom, prénom, email ou spécialité
        String searchTerm = "%" + query.toLowerCase() + "%";

        // Vous devez ajouter cette méthode dans MedecinRepository
        List<Medecin> medecins = medecinRepository.searchMedecins(searchTerm);

        return medecins.stream()
                .map(medecinMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedecinDTO> getMedecinsBySpecialite(String specialite) {
        log.info("Récupération de tous les médecins avec la spécialité {}", specialite);

        List<Medecin> medecins = medecinRepository.findBySpecialite(specialite);

        return medecins.stream()
                .map(medecinMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je ne sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<MedecinDTO> getMedecinsByDisponibilite(LocalDateTime dateHeure) {
        log.info("Récupération des médecins disponibles à la date et heure : {}", dateHeure);

        // Vous devez ajouter cette méthode dans MedecinRepository
        List<Medecin> medecins = medecinRepository.findAvailableAtDateTime(dateHeure);

        return medecins.stream()
                .map(medecinMapper::toDTO)
                .collect(Collectors.toList());
    }
}
