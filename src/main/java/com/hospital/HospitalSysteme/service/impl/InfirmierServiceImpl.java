package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.InfirmierCreationDTO;
import com.hospital.HospitalSysteme.dto.InfirmierDTO;
import com.hospital.HospitalSysteme.dto.PatientDTO;
import com.hospital.HospitalSysteme.dto.PlanDeSoinsDTO;
import com.hospital.HospitalSysteme.entity.Infirmier;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.entity.PlanDeSoins;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.*;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.InfirmierService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InfirmierServiceImpl implements InfirmierService {

    // Dependency Injection
    private PatientRepository patientRepository;
    private DossierMedicalRepository dossierMedicalRepository;
    private RendezVousRepository rendezVousRepository;
    private ConsultationRepository consultationRepository;
    private PrescriptionRepository prescriptionRepository;
    private PlanDeSoinsRepository planDeSoinsRepository;
    private FactureRepository factureRepository;
    private InfirmierRepository infirmierRepository;
//
    private PatientMapper patientMapper;
    private DossierMedicalMapper dossierMedicalMapper;
    private RendezVousMapper rendezVousMapper;
    private ConsultationMapper consultationMapper;
    private PrescriptionMapper prescriptionMapper;
    private PlanDeSoinsMapper planDeSoinsMapper;
    private FactureMapper factureMapper;
    private InfirmierMapper infirmierMapper;





    @Override
    public InfirmierDTO createInfirmier(InfirmierCreationDTO infirmierCreationDTO) {

        log.info("Création d'un nouveau infirmier avec l'email : {}", infirmierCreationDTO.getEmail());

        // Vérifier si l'email existe déjà
        if (infirmierRepository.existsByEmail(infirmierCreationDTO.getEmail())) {
            throw new IllegalArgumentException("Un infirmier avec cet email existe déjà : " + infirmierCreationDTO.getEmail());
        }

        // Convert infirmierCreationDTO to infirmier JPA Entity
        Infirmier infirmier = infirmierMapper.toEntity(infirmierCreationDTO);

        // infirmier JPA Entity
        Infirmier savedInfirmier = infirmierRepository.save(infirmier);

        log.info("Infirmier créé avec succès avec l'ID : {}", savedInfirmier.getId());

        // Convert saved infirmier JPA Entity into infirmierDTO object
        return infirmierMapper.toDTO(savedInfirmier);
    }

    @Override
    public InfirmierDTO getInfirmierById(Long infirmierId) {
        log.info("Récupération de l'infirmier avec l'ID : {}", infirmierId);

        Infirmier infirmier = infirmierRepository.findById(infirmierId).orElseThrow(
                () -> new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId)
        );

        return infirmierMapper.toDTO(infirmier);
    }

    @Override
    public List<InfirmierDTO> getAllInfirmiers() {
        log.info("Récupération de tous les infirmiers");

        List<Infirmier> infirmiers = infirmierRepository.findAll();

        return infirmiers.stream()
                .map((infirmier) -> infirmierMapper.toDTO(infirmier))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInfirmier(Long infirmierId) {
        log.info("Suppression de l'infirmier avec l'ID : {}", infirmierId);

        if (!infirmierRepository.existsById(infirmierId)){
            throw new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId);
        }

        infirmierRepository.deleteById(infirmierId);

        log.info("Infirmier supprimé avec succès");

    }

    // Je sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PlanDeSoinsDTO> getInfirmierPlansDeSoins(Long infirmierId) {
        log.info("Récupération des plans de soins pour l'infirmier avec l'ID : {}", infirmierId);

        // Vérifier que l'infirmier existe
        if (!infirmierRepository.existsById(infirmierId)) {
            throw new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId);
        }

        List<PlanDeSoins> plansDeSoins = planDeSoinsRepository.findByInfirmierId(infirmierId);

        return plansDeSoins.stream()
                .map(planDeSoinsMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PlanDeSoinsDTO> getInfirmierPlansDeSoinsByPatient(Long infirmierId, Long patientId) {
        log.info("Récupération des plans de soins pour l'infirmier avec l'ID : {} et le patient avec l'ID : {}", infirmierId, patientId);

        // Vérifier que l'infirmier existe
        if (!infirmierRepository.existsById(infirmierId)) {
            throw new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId);
        }
        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId);
        }

        List<PlanDeSoins> plansDeSoins = planDeSoinsRepository.findByInfirmierIdAndPatientId(infirmierId, patientId);

        return plansDeSoins.stream()
                .map(planDeSoinsMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PlanDeSoinsDTO> getInfirmierPlansDeSoinsByDate(Long infirmierId, LocalDate date) {
        log.info("Récupération des plans de soins pour l'infirmier avec l'ID : {} à la date : {}", infirmierId, date);

        // Vérifier que l'infirmier existe
        if (!infirmierRepository.existsById(infirmierId)) {
            throw new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId);
        }

        List<PlanDeSoins> plansDeSoins = planDeSoinsRepository.findByInfirmierIdAndDate(infirmierId, date);

        return plansDeSoins.stream()
                .map(planDeSoinsMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<PatientDTO> getInfirmierPatients(Long infirmierId) {
        log.info("Récupération des patients assignés à l'infirmier avec l'ID : {}", infirmierId);

        // Vérifier que l'infirmier existe
        if (!infirmierRepository.existsById(infirmierId)) {
            throw new ResourceNotFoundException("Infirmier non trouvé avec l'ID : " + infirmierId);
        }

        List<Patient> patients = patientRepository.findByInfirmierId(infirmierId);

        return patients.stream()
                .map(patientMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<InfirmierDTO> searchInfirmiers(String query) {
        log.info("Recherche d'infirmiers avec la requête : {}", query);

        String searchTerm = "%" + query.toLowerCase() + "%";

        List<Infirmier> infirmiers = infirmierRepository.searchInfirmiers(searchTerm);

        return infirmiers.stream()
                .map(infirmierMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Je sais pas
    // De la part de Claude pour s'inspirer
    @Override
    public List<InfirmierDTO> getInfirmiersByNiveauQualification(String niveauQualification) {
        log.info("Récupération des infirmiers avec le niveau de qualification : {}", niveauQualification);

        List<Infirmier> infirmiers = infirmierRepository.findByNiveauQualification(niveauQualification);

        return infirmiers.stream()
                .map(infirmierMapper::toDTO)
                .collect(Collectors.toList());
    }
}
