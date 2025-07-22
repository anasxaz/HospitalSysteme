package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.DepartementStatsDTO;
import com.hospital.HospitalSysteme.entity.Departement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartementStatsMapper {
    @Mapping(target = "departementId", source = "id")
    @Mapping(target = "nomDepartement", source = "nom")
    @Mapping(target = "nom", source = "nom") // Ajouté pour mapper aussi la propriété "nom"
    DepartementStatsDTO toStatsDTO(Departement departement);
}