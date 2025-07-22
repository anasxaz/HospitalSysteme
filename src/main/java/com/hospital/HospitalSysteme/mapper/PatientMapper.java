package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.PatientCreationDTO;
import com.hospital.HospitalSysteme.dto.PatientDTO;
import com.hospital.HospitalSysteme.dto.PatientDetailDTO;
import com.hospital.HospitalSysteme.dto.PatientUpdateDTO;
import com.hospital.HospitalSysteme.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PatientMapper {

    PatientDTO toDTO(Patient patient);
//    Optional<PatientDTO> toPatientDTO(Patient patient);
    PatientDetailDTO toDetailDTO(Patient patient);
    Patient toEntity(PatientDTO patientDTO);
    Patient toEntity(PatientCreationDTO patientCreationDTO);
    void updatePatientFromDTO(PatientUpdateDTO dto, @MappingTarget Patient patient);


}
