//package com.hospital.HospitalSysteme.mapper;
//
//import com.hospital.HospitalSysteme.dto.PrescriptionCreationDTO;
//import com.hospital.HospitalSysteme.dto.PrescriptionDTO;
//import com.hospital.HospitalSysteme.dto.PrescriptionUpdateDTO;
//import com.hospital.HospitalSysteme.entity.Consultation;
//import com.hospital.HospitalSysteme.entity.Medicament;
//import com.hospital.HospitalSysteme.entity.Prescription;
//import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
//import org.mapstruct.*;
//
//import java.util.Set;
//
//@Mapper(componentModel = "spring",
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
//        unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface PrescriptionMapper {
//
//    @Mapping(source = "consultation.id", target = "consultationId")
//    @Mapping(target = "medicamentId", expression = "java(getFirstMedicamentId(prescription.getMedicaments()))")
//    @Mapping(target = "medicamentNom", expression = "java(getFirstMedicamentNom(prescription.getMedicaments()))")
//    PrescriptionDTO toDTO(Prescription prescription);
//
//    @Mapping(target = "consultation", source = "consultationId", qualifiedByName = "mapConsultation")
//    @Mapping(target = "statut", constant = "ACTIVE")
//    @Mapping(target = "medicaments", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    Prescription toEntity(PrescriptionCreationDTO prescriptionCreationDTO);
//
//    @Mapping(target = "consultation", source = "consultationId", qualifiedByName = "mapConsultation")
//    @Mapping(target = "medicaments", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "statut", ignore = true)
//    void updatePrescriptionFromDTO(PrescriptionUpdateDTO prescriptionUpdateDTO, @MappingTarget Prescription prescription);
//
//    // Méthodes utilitaires
//    default Long getFirstMedicamentId(Set<Medicament> medicaments) {
//        return medicaments.isEmpty() ? null : medicaments.iterator().next().getId();
//    }
//
//    default String getFirstMedicamentNom(Set<Medicament> medicaments) {
//        return medicaments.isEmpty() ? null : medicaments.iterator().next().getNom();
//    }
//
//    @Named("mapConsultation")
//    default Consultation mapConsultation(Long consultationId) {
//        if (consultationId == null) {
//            return null;
//        }
//        Consultation consultation = new Consultation();
//        consultation.setId(consultationId);
//        return consultation;
//    }
//}

// 2ème tentative :
//package com.hospital.HospitalSysteme.mapper;
//
//import com.hospital.HospitalSysteme.dto.PrescriptionCreationDTO;
//import com.hospital.HospitalSysteme.dto.PrescriptionDTO;
//import com.hospital.HospitalSysteme.dto.PrescriptionUpdateDTO;
//import com.hospital.HospitalSysteme.entity.Consultation;
//import com.hospital.HospitalSysteme.entity.Medicament;
//import com.hospital.HospitalSysteme.entity.Prescription;
//import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
//import org.mapstruct.*;
//
//import java.util.Set;
//
//@Mapper(componentModel = "spring",
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
//        unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface PrescriptionMapper {
//
//    // Pour DTO -> Entity, mapper dateDebut vers date
//    @Mapping(source = "consultation.id", target = "consultationId")
//    @Mapping(source = "date", target = "dateDebut")  // ✅ AJOUT : mapper "date" vers "dateDebut"
//    @Mapping(target = "medicamentId", expression = "java(getFirstMedicamentId(prescription.getMedicaments()))")
//    @Mapping(target = "medicamentNom", expression = "java(getFirstMedicamentNom(prescription.getMedicaments()))")
//    PrescriptionDTO toDTO(Prescription prescription);
//
//    @Mapping(target = "consultation", source = "consultationId", qualifiedByName = "mapConsultation")
//    @Mapping(target = "statut", constant = "ACTIVE")
//    @Mapping(target = "medicaments", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    @Mapping(source = "dateDebut", target = "date")  // ✅ AJOUT : mapper "dateDebut" vers "date"
//    Prescription toEntity(PrescriptionCreationDTO prescriptionCreationDTO);
//
//    // Pour Update, mapper dateDebut vers date
//    @Mapping(target = "consultation", source = "consultationId", qualifiedByName = "mapConsultation")
//    @Mapping(target = "medicaments", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "statut", ignore = true)
//    @Mapping(source = "dateDebut", target = "date")  // ✅ AJOUT : mapper "dateDebut" vers "date"
//    void updatePrescriptionFromDTO(PrescriptionUpdateDTO prescriptionUpdateDTO, @MappingTarget Prescription prescription);
//
//    // Méthodes utilitaires
//    default Long getFirstMedicamentId(Set<Medicament> medicaments) {
//        return medicaments.isEmpty() ? null : medicaments.iterator().next().getId();
//    }
//
//    default String getFirstMedicamentNom(Set<Medicament> medicaments) {
//        return medicaments.isEmpty() ? null : medicaments.iterator().next().getNom();
//    }
//
//    @Named("mapConsultation")
//    default Consultation mapConsultation(Long consultationId) {
//        if (consultationId == null) {
//            return null;
//        }
//        Consultation consultation = new Consultation();
//        consultation.setId(consultationId);
//        return consultation;
//    }
//}



package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.PrescriptionCreationDTO;
import com.hospital.HospitalSysteme.dto.PrescriptionDTO;
import com.hospital.HospitalSysteme.dto.PrescriptionUpdateDTO;
import com.hospital.HospitalSysteme.entity.Consultation;
import com.hospital.HospitalSysteme.entity.Medicament;
import com.hospital.HospitalSysteme.entity.Prescription;
import com.hospital.HospitalSysteme.entity.enums.StatutPrescription;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrescriptionMapper {

    // ✅ SIMPLIFIÉ : Plus besoin de mapping manuel des dates !
    // Entité.dateDebut <-> DTO.dateDebut (noms identiques)
    @Mapping(source = "consultation.id", target = "consultationId")
    @Mapping(target = "medicamentId", expression = "java(getFirstMedicamentId(prescription.getMedicaments()))")
    @Mapping(target = "medicamentNom", expression = "java(getFirstMedicamentNom(prescription.getMedicaments()))")
    PrescriptionDTO toDTO(Prescription prescription);

    // ✅ SIMPLIFIÉ : MapStruct fait le mapping automatiquement
    @Mapping(target = "consultation", source = "consultationId", qualifiedByName = "mapConsultation")
    @Mapping(target = "statut", constant = "ACTIVE")
    @Mapping(target = "medicaments", ignore = true)
    @Mapping(target = "id", ignore = true)
    Prescription toEntity(PrescriptionCreationDTO prescriptionCreationDTO);

    // ✅ SIMPLIFIÉ : Mapping automatique des dates
    @Mapping(target = "consultation", source = "consultationId", qualifiedByName = "mapConsultation")
    @Mapping(target = "medicaments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", ignore = true)
    void updatePrescriptionFromDTO(PrescriptionUpdateDTO prescriptionUpdateDTO, @MappingTarget Prescription prescription);

    // Méthodes utilitaires
    default Long getFirstMedicamentId(Set<Medicament> medicaments) {
        return medicaments.isEmpty() ? null : medicaments.iterator().next().getId();
    }

    default String getFirstMedicamentNom(Set<Medicament> medicaments) {
        return medicaments.isEmpty() ? null : medicaments.iterator().next().getNom();
    }

    @Named("mapConsultation")
    default Consultation mapConsultation(Long consultationId) {
        if (consultationId == null) {
            return null;
        }
        Consultation consultation = new Consultation();
        consultation.setId(consultationId);
        return consultation;
    }
}