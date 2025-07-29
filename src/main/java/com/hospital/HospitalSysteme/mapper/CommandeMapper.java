package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.CommandeCreationDTO;
import com.hospital.HospitalSysteme.dto.CommandeDTO;
import com.hospital.HospitalSysteme.dto.CommandeUpdateDTO;
import com.hospital.HospitalSysteme.entity.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

//@Mapper(componentModel = "spring")
//public interface CommandeMapper {
//
//    @Mapping(source = "cadreAdministratif.id", target = "cadreAdministratifId")
//    @Mapping(source = "cadreAdministratif.nom", target = "cadreAdministratifNom")
//    CommandeDTO toDTO(Commande commande);
//
//    List<CommandeDTO> toDTOList(List<Commande> commandes);
//
//    @Mapping(source = "cadreAdministratifId", target = "cadreAdministratif.id")
//    Commande toEntity(CommandeCreationDTO commandeCreationDTO);
//
//    void updateCommandeFromDTO(CommandeUpdateDTO commandeUpdateDTO, @MappingTarget Commande commande);
//}
@Mapper(componentModel = "spring")
public interface CommandeMapper {

    @Mapping(source = "cadreAdministratif.id", target = "cadreAdministratifId")
    @Mapping(source = "cadreAdministratif.nom", target = "cadreAdministratifNom")
    @Mapping(source = "cadreAdministratif.prenom", target = "cadreAdministratifPrenom")  // ← AJOUTER
    CommandeDTO toDTO(Commande commande);

    List<CommandeDTO> toDTOList(List<Commande> commandes);

    @Mapping(source = "cadreAdministratifId", target = "cadreAdministratif.id")
    Commande toEntity(CommandeCreationDTO commandeCreationDTO);

    void updateCommandeFromDTO(CommandeUpdateDTO commandeUpdateDTO, @MappingTarget Commande commande);
}