package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.DepartementDTO;
import com.hospital.HospitalSysteme.dto.DepartementCreationDTO;
import com.hospital.HospitalSysteme.dto.DepartementUpdateDTO;
import com.hospital.HospitalSysteme.dto.PatientUpdateDTO;
import com.hospital.HospitalSysteme.entity.Departement;
import com.hospital.HospitalSysteme.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartementMapper {
    DepartementDTO toDTO(Departement departement);
    Departement toEntity(DepartementDTO departementDTO);
    Departement toEntity(DepartementCreationDTO departementCreationDTO);
    void updateDepartementFromDTO(DepartementUpdateDTO dto, @MappingTarget Departement departement);
}