package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.MedicamentCreationDTO;
import com.hospital.HospitalSysteme.dto.MedicamentDTO;
import com.hospital.HospitalSysteme.dto.MedicamentUpdateDTO;
import com.hospital.HospitalSysteme.entity.Medicament;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.MedicamentMapper;
import com.hospital.HospitalSysteme.repository.MedicamentRepository;
import com.hospital.HospitalSysteme.repository.PrescriptionRepository;
import com.hospital.HospitalSysteme.service.MedicamentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MedicamentServiceImpl implements MedicamentService {

    private MedicamentRepository medicamentRepository;
    private PrescriptionRepository prescriptionRepository;
    private MedicamentMapper medicamentMapper;

    @Override
    public MedicamentDTO createMedicament(MedicamentCreationDTO medicamentCreationDTO) {
        log.info("Création d'un nouveau médicament : {}", medicamentCreationDTO.getNom());

        Medicament medicament = medicamentMapper.toEntity(medicamentCreationDTO);
        Medicament savedMedicament = medicamentRepository.save(medicament);

        log.info("Médicament créé avec succès avec l'ID : {}", savedMedicament.getId());

        return medicamentMapper.toDTO(savedMedicament);
    }

    @Override
    public MedicamentDTO getMedicamentById(Long medicamentId) {
        log.info("Récupération du médicament avec l'ID : {}", medicamentId);

        Medicament medicament = medicamentRepository.findById(medicamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'ID : " + medicamentId));

        return medicamentMapper.toDTO(medicament);
    }

    @Override
    public List<MedicamentDTO> getAllMedicaments() {
        log.info("Récupération de tous les médicaments");

        List<Medicament> medicaments = medicamentRepository.findAll();

        return medicaments.stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedicamentDTO updateMedicament(Long medicamentId, MedicamentUpdateDTO medicamentUpdateDTO) {
        log.info("Mise à jour du médicament avec l'ID : {}", medicamentId);

        Medicament medicament = medicamentRepository.findById(medicamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'ID : " + medicamentId));

        medicamentMapper.updateMedicamentFromDTO(medicamentUpdateDTO, medicament);
        Medicament updatedMedicament = medicamentRepository.save(medicament);

        log.info("Médicament mis à jour avec succès");

        return medicamentMapper.toDTO(updatedMedicament);
    }

    @Override
    public void deleteMedicament(Long medicamentId) {
        log.info("Suppression du médicament avec l'ID : {}", medicamentId);

        if (!medicamentRepository.existsById(medicamentId)) {
            throw new ResourceNotFoundException("Médicament non trouvé avec l'ID : " + medicamentId);
        }

        medicamentRepository.deleteById(medicamentId);

        log.info("Médicament supprimé avec succès");
    }

    @Override
    public List<MedicamentDTO> searchMedicamentsByNom(String nom) {
        log.info("Recherche de médicaments par nom : {}", nom);

        List<Medicament> medicaments = medicamentRepository.findByNomContainingIgnoreCase(nom);

        return medicaments.stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicamentDTO> getMedicamentsByCategorie(String categorie) {
        log.info("Récupération des médicaments par catégorie : {}", categorie);

        List<Medicament> medicaments = medicamentRepository.findByCategorie(categorie);

        return medicaments.stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicamentDTO> getMedicamentsByFabricant(String fabricant) {
        log.info("Récupération des médicaments par fabricant : {}", fabricant);

        List<Medicament> medicaments = medicamentRepository.findByFabricant(fabricant);

        return medicaments.stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicamentDTO> getMedicamentsByOrdonnanceRequise(Boolean ordonnanceRequise) {
        log.info("Récupération des médicaments par ordonnance requise : {}", ordonnanceRequise);

        List<Medicament> medicaments = medicamentRepository.findByOrdonnanceRequise(ordonnanceRequise);

        return medicaments.stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicamentDTO> getMedicamentsByPrescription(Long prescriptionId) {
        log.info("Récupération des médicaments pour la prescription avec l'ID : {}", prescriptionId);

        if (!prescriptionRepository.existsById(prescriptionId)) {
            throw new ResourceNotFoundException("Prescription non trouvée avec l'ID : " + prescriptionId);
        }

        List<Medicament> medicaments = medicamentRepository.findByPrescriptionsId(prescriptionId);

        return medicaments.stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int countMedicamentsByCategorie(String categorie) {
        log.info("Comptage des médicaments par catégorie : {}", categorie);
        return medicamentRepository.countByCategorie(categorie);
    }

    @Override
    public int countMedicamentsByFabricant(String fabricant) {
        log.info("Comptage des médicaments par fabricant : {}", fabricant);
        return medicamentRepository.countByFabricant(fabricant);
    }

    @Override
    public List<String> getAllCategories() {
        log.info("Récupération de toutes les catégories de médicaments");
        return medicamentRepository.findDistinctCategories();
    }

    @Override
    public List<String> getAllFabricants() {
        log.info("Récupération de tous les fabricants de médicaments");
        return medicamentRepository.findDistinctFabricants();
    }
}