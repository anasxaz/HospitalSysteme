package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.CadreAdministratifDTO;
import com.hospital.HospitalSysteme.dto.CadreAdministratifCreationDTO;
import com.hospital.HospitalSysteme.entity.CadreAdministratif;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CadreAdministratifMapper {
    CadreAdministratifDTO toDTO(CadreAdministratif cadreAdministratif);
    CadreAdministratif toEntity(CadreAdministratifDTO cadreAdministratifDTO);
    CadreAdministratif toEntity(CadreAdministratifCreationDTO cadreAdministratifCreationDTO);
}