package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.CadreAdministratifDTO;
import com.hospital.HospitalSysteme.dto.CadreAdministratifCreationDTO;
import com.hospital.HospitalSysteme.entity.CadreAdministratif;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

//@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface CadreAdministratifMapper {
//    CadreAdministratifDTO toDTO(CadreAdministratif cadreAdministratif);
//    CadreAdministratif toEntity(CadreAdministratifDTO cadreAdministratifDTO);
//    CadreAdministratif toEntity(CadreAdministratifCreationDTO cadreAdministratifCreationDTO);
//}

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CadreAdministratifMapper {

    @Mapping(source = "departement.id", target = "departementId")
    @Mapping(source = "departement.nom", target = "departementNom")
    CadreAdministratifDTO toDTO(CadreAdministratif cadreAdministratif);

    CadreAdministratif toEntity(CadreAdministratifDTO cadreAdministratifDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "factures", ignore = true)
    @Mapping(target = "plannings", ignore = true)
    @Mapping(target = "commandes", ignore = true)
    @Mapping(target = "departement", ignore = true)
    CadreAdministratif toEntity(CadreAdministratifCreationDTO cadreAdministratifCreationDTO);
}