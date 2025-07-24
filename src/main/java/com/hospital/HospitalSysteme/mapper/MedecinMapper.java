package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.MedecinDTO;
import com.hospital.HospitalSysteme.dto.MedecinCreationDTO;
import com.hospital.HospitalSysteme.entity.Medecin;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class MedecinMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    // Mapping de base
    @Mapping(target = "numeroOrdre", source = "numeroLicence")
    @Mapping(target = "departementId", source = "departement.id")
    @Mapping(target = "departementNom", source = "departement.nom")
    public abstract MedecinDTO toDTO(Medecin medecin);

    public abstract Medecin toEntity(MedecinDTO medecinDTO);

    // Mapping pour la création avec encodage du mot de passe
    @Mapping(target = "numeroLicence", source = "numeroOrdre")
    @Mapping(target = "departement", ignore = true)
    @Mapping(target = "poste", ignore = true)
    @Mapping(target = "dateEmbauche", ignore = true)
    @Mapping(target = "salaire", ignore = true)
    @Mapping(target = "rendezVous", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(medecinCreationDTO.getPassword()))")
    public abstract Medecin toEntity(MedecinCreationDTO medecinCreationDTO);


}