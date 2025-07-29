//package com.hospital.HospitalSysteme.mapper;
//
//import com.hospital.HospitalSysteme.dto.InfirmierDTO;
//import com.hospital.HospitalSysteme.dto.InfirmierCreationDTO;
//import com.hospital.HospitalSysteme.entity.Infirmier;
//import org.mapstruct.Mapper;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//
//@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface InfirmierMapper {
//    InfirmierDTO toDTO(Infirmier infirmier);
//    Infirmier toEntity(InfirmierDTO infirmierDTO);
//    Infirmier toEntity(InfirmierCreationDTO infirmierCreationDTO);
//}


package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.InfirmierDTO;
import com.hospital.HospitalSysteme.dto.InfirmierCreationDTO;
import com.hospital.HospitalSysteme.entity.Infirmier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InfirmierMapper {

    @Mapping(source = "departement.id", target = "departementId")
    @Mapping(source = "departement.nom", target = "departementNom")
    InfirmierDTO toDTO(Infirmier infirmier);

    Infirmier toEntity(InfirmierDTO infirmierDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "planDeSoins", ignore = true)
    @Mapping(target = "departement", ignore = true)
    Infirmier toEntity(InfirmierCreationDTO infirmierCreationDTO);
}