package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.InfirmierDTO;
import com.hospital.HospitalSysteme.dto.InfirmierCreationDTO;
import com.hospital.HospitalSysteme.entity.Infirmier;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InfirmierMapper {
    InfirmierDTO toDTO(Infirmier infirmier);
    Infirmier toEntity(InfirmierDTO infirmierDTO);
    Infirmier toEntity(InfirmierCreationDTO infirmierCreationDTO);
}