package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.PrescriptionCreationDTO;
import com.hospital.HospitalSysteme.dto.PrescriptionDTO;
import com.hospital.HospitalSysteme.dto.PrescriptionUpdateDTO;
import com.hospital.HospitalSysteme.entity.Medicament;
import com.hospital.HospitalSysteme.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrescriptionMapper {

    @Mapping(source = "consultation.id", target = "consultationId")
    @Mapping(source = "date", target = "dateDebut")
    @Mapping(target = "medicamentId", expression = "java(getFirstMedicamentId(prescription.getMedicaments()))")
    @Mapping(target = "medicamentNom", expression = "java(getFirstMedicamentNom(prescription.getMedicaments()))")
    PrescriptionDTO toDTO(Prescription prescription);

    @Mapping(source = "consultationId", target = "consultation.id")
    @Mapping(source = "dateDebut", target = "date")
    @Mapping(target = "statut", constant = "ACTIVE")
    @Mapping(target = "medicaments", ignore = true)
    @Mapping(target = "id", ignore = true)
    Prescription toEntity(PrescriptionCreationDTO prescriptionCreationDTO);

    void updatePrescriptionFromDTO(PrescriptionUpdateDTO prescriptionUpdateDTO, @MappingTarget Prescription prescription);

    // Méthodes utilitaires
    default Long getFirstMedicamentId(Set<Medicament> medicaments) {
        return medicaments.isEmpty() ? null : medicaments.iterator().next().getId();
    }

    default String getFirstMedicamentNom(Set<Medicament> medicaments) {
        return medicaments.isEmpty() ? null : medicaments.iterator().next().getNom();
    }
}