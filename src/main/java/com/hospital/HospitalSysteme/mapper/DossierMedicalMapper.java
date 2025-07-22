package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.DossierMedical;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.dto.PatientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, ConsultationMapper.class, PrescriptionMapper.class})
public interface DossierMedicalMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    DossierMedicalDTO toDTO(DossierMedical dossierMedical);

    DossierMedical toEntity(DossierMedicalDTO dossierMedicalDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "patientId", target = "patient.id")
    DossierMedical toEntity(DossierMedicalCreationDTO dossierMedicalCreationDTO);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    @Mapping(source = "consultations", target = "consultationDTOS")
    // Supprimez cette ligne ou remplacez par le nom correct de la propriété
    // @Mapping(source = "prescriptions", target = "prescriptionDTOS")
    @Mapping(target = "prescriptionDTOS", ignore = true) // Ignorer cette propriété pour l'instant
    @Mapping(source = "patient", target = "patient")
    DossierMedicalDetailDTO toDetailDTO(DossierMedical dossierMedical);

    void updateDossierMedicalFromDTO(DossierMedicalCreationDTO dossierMedicalUpdateDTO, @MappingTarget DossierMedical dossierMedical);
}