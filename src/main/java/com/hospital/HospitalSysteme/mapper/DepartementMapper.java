package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.DepartementDTO;
import com.hospital.HospitalSysteme.dto.DepartementCreationDTO;
import com.hospital.HospitalSysteme.dto.DepartementUpdateDTO;
import com.hospital.HospitalSysteme.dto.PatientUpdateDTO;
import com.hospital.HospitalSysteme.entity.Departement;
import com.hospital.HospitalSysteme.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartementMapper {
    @Mapping(target = "chefDepartementId", source = "chefDepartement.id")
    DepartementDTO toDTO(Departement departement);

    Departement toEntity(DepartementDTO departementDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personnels", ignore = true)
    @Mapping(target = "chefDepartement", ignore = true)
    @Mapping(target = "dateCreation", expression = "java(java.time.LocalDateTime.now())")
    Departement toEntity(DepartementCreationDTO departementCreationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personnels", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "chefDepartement", ignore = true)
    void updateDepartementFromDTO(DepartementUpdateDTO updateDTO, @MappingTarget Departement departement);
}