package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.ConsultationDTO;
import com.hospital.HospitalSysteme.dto.ConsultationCreationDTO;
import com.hospital.HospitalSysteme.dto.ConsultationSummaryDTO;
import com.hospital.HospitalSysteme.dto.ConsultationUpdateDTO;
import com.hospital.HospitalSysteme.entity.Consultation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ConsultationMapper {
    ConsultationDTO toDTO(Consultation consultation);
    Consultation toEntity(ConsultationDTO consultationDTO);
    Consultation toEntity(ConsultationCreationDTO consultationCreationDTO);
    void updateConsultationFromDTO(ConsultationUpdateDTO consultationUpdateDTO, @MappingTarget Consultation consultation);

    @Mapping(target = "patientId", source = "dossierMedical.patient.id")
    @Mapping(target = "patientNom", source = "dossierMedical.patient.nom")
    @Mapping(target = "patientPrenom", source = "dossierMedical.patient.prenom")
    @Mapping(target = "medecinId", source = "medecin.id")
    @Mapping(target = "medecinNom", source = "medecin.nom")
    @Mapping(target = "medecinPrenom", source = "medecin.prenom")
    ConsultationSummaryDTO toSummaryDTO(Consultation consultation);

}