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

    // ✅ DTO standard - CORRIGÉ pour mapper rendezVousId
    @Mapping(source = "medecin.id", target = "medecinId")
    @Mapping(source = "medecin.nom", target = "medecinNom")
    @Mapping(source = "medecin.prenom", target = "medecinPrenom")
    @Mapping(source = "dossierMedical.id", target = "dossierMedicalId")
    @Mapping(source = "rendezVous.id", target = "rendezVousId") // ✅ CORRIGÉ !
    ConsultationDTO toDTO(Consultation consultation);

    // ✅ Conversion DTO vers entité
    @Mapping(target = "medecin", ignore = true)
    @Mapping(target = "dossierMedical", ignore = true)
    @Mapping(target = "rendezVous", ignore = true)
    @Mapping(target = "recommandations", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    Consultation toEntity(ConsultationDTO consultationDTO);

    // ✅ Création - CORRIGÉ sans date
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)           // ✅ IGNORÉ - sera défini manuellement
    @Mapping(target = "medecin", ignore = true)
    @Mapping(target = "dossierMedical", ignore = true)
    @Mapping(target = "rendezVous", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "recommandations", ignore = true)
    Consultation toEntity(ConsultationCreationDTO consultationCreationDTO);

    // ✅ Mise à jour - SELON VOTRE ConsultationUpdateDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medecin", ignore = true)
    @Mapping(target = "dossierMedical", ignore = true)
    @Mapping(target = "rendezVous", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(source = "dateConsultation", target = "date")
    @Mapping(source = "motif", target = "symptomes")
    void updateConsultationFromDTO(ConsultationUpdateDTO consultationUpdateDTO, @MappingTarget Consultation consultation);

    // ✅ Summary DTO
    @Mapping(target = "patientId", source = "dossierMedical.patient.id")
    @Mapping(target = "patientNom", source = "dossierMedical.patient.nom")
    @Mapping(target = "patientPrenom", source = "dossierMedical.patient.prenom")
    @Mapping(target = "medecinId", source = "medecin.id")
    @Mapping(target = "medecinNom", source = "medecin.nom")
    @Mapping(target = "medecinPrenom", source = "medecin.prenom")
    @Mapping(source = "date", target = "dateConsultation")
    ConsultationSummaryDTO toSummaryDTO(Consultation consultation);
}