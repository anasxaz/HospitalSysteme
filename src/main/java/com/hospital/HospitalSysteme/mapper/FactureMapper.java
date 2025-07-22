package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.FactureDTO;
import com.hospital.HospitalSysteme.dto.FactureCreationDTO;
import com.hospital.HospitalSysteme.dto.FactureDetailDTO;
import com.hospital.HospitalSysteme.entity.Facture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, CadreAdministratifMapper.class, ServiceMapper.class})
public interface FactureMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    @Mapping(source = "cadreAdministratif.id", target = "cadreAdministratifId")
    @Mapping(source = "cadreAdministratif.nom", target = "cadreAdministratifNom")
    FactureDTO toDTO(Facture facture);


    Facture toEntity(FactureDTO factureDTO);
    Facture toEntity(FactureCreationDTO factureCreationDTO);
//    void updateFactureFromDTO(FactureUpdateDTO dto, @MappingTarget Facture facture);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    @Mapping(source = "cadreAdministratif.id", target = "cadreAdministratifId")
    @Mapping(source = "cadreAdministratif.nom", target = "cadreAdministratifNom")
    @Mapping(source = "serviceHospitaliers", target = "serviceDTOS") // Changez "services" par le nom correct
    @Mapping(source = "patient", target = "patientDTO") // Changez "patient" par le nom correct
        // Supprimez cette ligne qui est en double
        // @Mapping(source = "cadreAdministratif.id", target = "cadreAdministratifId")
    FactureDetailDTO toDetailDTO(Facture facture);
}