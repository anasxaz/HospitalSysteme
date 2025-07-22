//package com.hospital.HospitalSysteme.mapper.impl;
//
//import com.hospital.HospitalSysteme.dto.CadreAdministratifCreationDTO;
//import com.hospital.HospitalSysteme.dto.CadreAdministratifDTO;
//import com.hospital.HospitalSysteme.entity.CadreAdministratif;
//import com.hospital.HospitalSysteme.mapper.CadreAdministratifMapper;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CadreAdministratifMapperImpl implements CadreAdministratifMapper {
//
//    @Override
//    public CadreAdministratifDTO toDTO(CadreAdministratif cadreAdministratif) {
//        if (cadreAdministratif == null) {
//            return null;
//        }
//
//        CadreAdministratifDTO dto = new CadreAdministratifDTO();
//
//        // Copier les propriétés de User
//        dto.setId(cadreAdministratif.getId());
//        dto.setNom(cadreAdministratif.getNom());
//        dto.setPrenom(cadreAdministratif.getPrenom());
//        dto.setEmail(cadreAdministratif.getEmail());
//        dto.setTelephone(cadreAdministratif.getTelephone());
//        dto.setAdresse(cadreAdministratif.getAdresse());
//        dto.setDateNaissance(cadreAdministratif.getDateNaissance());
//        dto.setGenre(cadreAdministratif.getGenre());
//        dto.setDateCreation(cadreAdministratif.getDateCreation());
//        dto.setDerniereConnexion(cadreAdministratif.getDerniereConnexion());
//
//        // Copier les propriétés spécifiques à CadreAdministratif
//        dto.setFonction(cadreAdministratif.getFonction());
//
//        if (cadreAdministratif.getDepartement() != null) {
//            dto.setDepartementId(cadreAdministratif.getDepartement().getId());
//            dto.setDepartementNom(cadreAdministratif.getDepartement().getNom());
//        }
//
//        return dto;
//    }
//
//    @Override
//    public CadreAdministratif toEntity(CadreAdministratifDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        CadreAdministratif cadreAdministratif = new CadreAdministratif();
//
//        // Copier les propriétés de User
//        cadreAdministratif.setId(dto.getId());
//        cadreAdministratif.setNom(dto.getNom());
//        cadreAdministratif.setPrenom(dto.getPrenom());
//        cadreAdministratif.setEmail(dto.getEmail());
//        cadreAdministratif.setTelephone(dto.getTelephone());
//        cadreAdministratif.setAdresse(dto.getAdresse());
//        cadreAdministratif.setDateNaissance(dto.getDateNaissance());
//        cadreAdministratif.setGenre(dto.getGenre());
//        cadreAdministratif.setDateCreation(dto.getDateCreation());
//        cadreAdministratif.setDerniereConnexion(dto.getDerniereConnexion());
//
//        // Copier les propriétés spécifiques à CadreAdministratif
//        cadreAdministratif.setFonction(dto.getFonction());
//
//        // Les relations (departement) doivent être gérées par le service
//        // car nous avons besoin des repositories pour charger les entités
//
//        return cadreAdministratif;
//    }
//
//    @Override
//    public CadreAdministratif toEntity(CadreAdministratifCreationDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        CadreAdministratif cadreAdministratif = new CadreAdministratif();
//
//        // Copier les propriétés de User
//        cadreAdministratif.setNom(dto.getNom());
//        cadreAdministratif.setPrenom(dto.getPrenom());
//        cadreAdministratif.setEmail(dto.getEmail());
//        cadreAdministratif.setTelephone(dto.getTelephone());
//        cadreAdministratif.setAdresse(dto.getAdresse());
//        cadreAdministratif.setDateNaissance(dto.getDateNaissance());
//        cadreAdministratif.setGenre(dto.getGenre());
//
//        // Le mot de passe doit être géré par le service avec un encodeur
//        // cadreAdministratif.setPassword(passwordEncoder.encode(dto.getPassword()));
//
//        // Copier les propriétés spécifiques à CadreAdministratif
//        cadreAdministratif.setFonction(dto.getFonction());
//
//        // Les relations (departement) doivent être gérées par le service
//        // car nous avons besoin des repositories pour charger les entités
//
//        return cadreAdministratif;
//    }
//}