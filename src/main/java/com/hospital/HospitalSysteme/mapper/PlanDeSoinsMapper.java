package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.PlanDeSoinsDTO;
import com.hospital.HospitalSysteme.dto.PlanDeSoinsCreationDTO;
import com.hospital.HospitalSysteme.dto.PlanDeSoinsUpdateDTO;
import com.hospital.HospitalSysteme.entity.PlanDeSoins;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, InfirmierMapper.class})
public interface PlanDeSoinsMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    @Mapping(source = "infirmier.id", target = "infirmierId")
    @Mapping(source = "infirmier.nom", target = "infirmierNom")
    @Mapping(source = "infirmier.prenom", target = "infirmierPrenom")
    PlanDeSoinsDTO toDTO(PlanDeSoins planDeSoins);


    PlanDeSoins toEntity(PlanDeSoinsDTO planDeSoinsDTO);

    @Mapping(source = "patientId", target = "patient.id")
    @Mapping(source = "infirmierId", target = "infirmier.id")
    @Mapping(target = "statut", constant = "EN_COURS")
    PlanDeSoins toEntity(PlanDeSoinsCreationDTO planDeSoinsCreationDTO);

    void updatePlanDeSoinsFromDTO(PlanDeSoinsUpdateDTO dto, @MappingTarget PlanDeSoins planDeSoins);


}