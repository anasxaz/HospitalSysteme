package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.MedecinDTO;
import com.hospital.HospitalSysteme.dto.MedecinCreationDTO;
import com.hospital.HospitalSysteme.entity.Medecin;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MedecinMapper {
    MedecinDTO toDTO(Medecin medecin);
    Medecin toEntity(MedecinDTO medecinDTO);
    Medecin toEntity(MedecinCreationDTO medecinCreationDTO);
}