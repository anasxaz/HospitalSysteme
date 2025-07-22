package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.PrescriptionDTO;
import com.hospital.HospitalSysteme.dto.PrescriptionCreationDTO;
import com.hospital.HospitalSysteme.dto.PrescriptionUpdateDTO;
import com.hospital.HospitalSysteme.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrescriptionMapper {
    PrescriptionDTO toDTO(Prescription prescription);
    Prescription toEntity(PrescriptionDTO prescriptionDTO);
    Prescription toEntity(PrescriptionCreationDTO prescriptionCreationDTO);
    void updatePrescriptionFromDTO(PrescriptionUpdateDTO prescriptionUpdateDTO, @MappingTarget Prescription prescription);
//    void updatePrescriptionFromDTO(PrescriptionUpdateDTO dto, @MappingTarget Prescription prescription);
}