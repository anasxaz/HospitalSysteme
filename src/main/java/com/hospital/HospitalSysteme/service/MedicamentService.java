package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.MedicamentCreationDTO;
import com.hospital.HospitalSysteme.dto.MedicamentDTO;
import com.hospital.HospitalSysteme.dto.MedicamentUpdateDTO;

import java.util.List;

public interface MedicamentService {
    MedicamentDTO createMedicament(MedicamentCreationDTO medicamentCreationDTO);
    MedicamentDTO getMedicamentById(Long medicamentId);
    List<MedicamentDTO> getAllMedicaments();
    MedicamentDTO updateMedicament(Long medicamentId, MedicamentUpdateDTO medicamentUpdateDTO);
    void deleteMedicament(Long medicamentId);

    List<MedicamentDTO> searchMedicamentsByNom(String nom);
    List<MedicamentDTO> getMedicamentsByCategorie(String categorie);
    List<MedicamentDTO> getMedicamentsByFabricant(String fabricant);
    List<MedicamentDTO> getMedicamentsByOrdonnanceRequise(Boolean ordonnanceRequise);
    List<MedicamentDTO> getMedicamentsByPrescription(Long prescriptionId);

    int countMedicamentsByCategorie(String categorie);
    int countMedicamentsByFabricant(String fabricant);

    List<String> getAllCategories();
    List<String> getAllFabricants();
}