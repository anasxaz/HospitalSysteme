//package com.hospital.HospitalSysteme.mapper;
//
//import com.hospital.HospitalSysteme.dto.*;
//import com.hospital.HospitalSysteme.entity.DossierMedical;
//import com.hospital.HospitalSysteme.entity.Patient;
//import com.hospital.HospitalSysteme.dto.PatientDTO;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//
//@Mapper(componentModel = "spring", uses = {PatientMapper.class, ConsultationMapper.class, PrescriptionMapper.class})
//public interface DossierMedicalMapper {
//
//    @Mapping(source = "patient.id", target = "patientId")
//    @Mapping(source = "patient.nom", target = "patientNom")
//    @Mapping(source = "patient.prenom", target = "patientPrenom")
//    DossierMedicalDTO toDTO(DossierMedical dossierMedical);
//
//    DossierMedical toEntity(DossierMedicalDTO dossierMedicalDTO);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(source = "patientId", target = "patient.id")
//    DossierMedical toEntity(DossierMedicalCreationDTO dossierMedicalCreationDTO);
//
//    @Mapping(source = "patient.id", target = "patientId")
//    @Mapping(source = "patient.nom", target = "patientNom")
//    @Mapping(source = "patient.prenom", target = "patientPrenom")
//    @Mapping(source = "consultations", target = "consultationDTOS")
//    // Supprimez cette ligne ou remplacez par le nom correct de la propriété
//    // @Mapping(source = "prescriptions", target = "prescriptionDTOS")
//    @Mapping(target = "prescriptionDTOS", ignore = true) // Ignorer cette propriété pour l'instant
//    @Mapping(source = "patient", target = "patient")
//    DossierMedicalDetailDTO toDetailDTO(DossierMedical dossierMedical);
//
//    void updateDossierMedicalFromDTO(DossierMedicalCreationDTO dossierMedicalUpdateDTO, @MappingTarget DossierMedical dossierMedical);
//}


package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.DossierMedical;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.dto.PatientDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, ConsultationMapper.class, PrescriptionMapper.class})
public interface DossierMedicalMapper {

    // Lecture standard
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    DossierMedicalDTO toDTO(DossierMedical dossierMedical);

    // Conversion DTO -> Entity
    DossierMedical toEntity(DossierMedicalDTO dossierMedicalDTO);

    // Création - CORRIGÉ
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateMiseAJour", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "patient", ignore = true) // On le définit manuellement dans le service
    @Mapping(target = "groupeSanguin", ignore = true) // Pas dans le CreationDTO
    DossierMedical toEntity(DossierMedicalCreationDTO dossierMedicalCreationDTO);

    // Lecture détaillée
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    @Mapping(source = "consultations", target = "consultationDTOS")
    @Mapping(target = "prescriptionDTOS", ignore = true) // Ignorer pour l'instant
    @Mapping(source = "patient", target = "patient")
    DossierMedicalDetailDTO toDetailDTO(DossierMedical dossierMedical);

    // Mise à jour - NOUVELLE MÉTHODE AVEC LE BON DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateMiseAJour", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "patient", ignore = true)
    void updateDossierMedicalFromDTO(DossierMedicalUpdateDTO dossierMedicalUpdateDTO, @MappingTarget DossierMedical dossierMedical);

    // ANCIENNE MÉTHODE - À GARDER POUR LA COMPATIBILITÉ MAIS AMÉLIORER
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateMiseAJour", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "groupeSanguin", ignore = true) // Pas dans le CreationDTO
    void updateDossierMedicalFromCreationDTO(DossierMedicalCreationDTO dossierMedicalCreationDTO, @MappingTarget DossierMedical dossierMedical);
}