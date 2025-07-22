//package com.hospital.HospitalSysteme.mapper.impl;
//
//import com.hospital.HospitalSysteme.dto.*;
//import com.hospital.HospitalSysteme.entity.CadreAdministratif;
//import com.hospital.HospitalSysteme.entity.Facture;
//import com.hospital.HospitalSysteme.entity.Patient;
//import com.hospital.HospitalSysteme.entity.ServiceHospitalier;
//import com.hospital.HospitalSysteme.mapper.FactureMapper;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class FactureMapperImpl implements FactureMapper {
//
//    @Override
//    public FactureDTO toDTO(Facture facture) {
//        if (facture == null) {
//            return null;
//        }
//
//        FactureDTO dto = new FactureDTO();
//        dto.setId(facture.getId());
//        dto.setNumero(facture.getNumero());
//        dto.setDate(facture.getDate());
//        dto.setMontantTotal(facture.getMontantTotal());
//        dto.setStatutPaiement(facture.getStatutPaiement());
//        dto.setMethodePaiement(facture.getMethodePaiement());
//
//        if (facture.getPatient() != null) {
//            Patient patient = facture.getPatient();
//            dto.setPatientId(patient.getId());
//            dto.setPatientNom(patient.getNom());
//            dto.setPatientPrenom(patient.getPrenom());
//        }
//
//        if (facture.getCadreAdministratif() != null) {
//            CadreAdministratif cadre = facture.getCadreAdministratif();
//            dto.setCadreAdministratifId(cadre.getId());
//            dto.setCadreAdministratifNom(cadre.getNom());
//        }
//
//        return dto;
//    }
//
//    @Override
//    public Facture toEntity(FactureDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        Facture facture = new Facture();
//        facture.setId(dto.getId());
//        facture.setNumero(dto.getNumero());
//        facture.setDate(dto.getDate());
//        facture.setMontantTotal(dto.getMontantTotal());
//        facture.setStatutPaiement(dto.getStatutPaiement());
//        facture.setMethodePaiement(dto.getMethodePaiement());
//
//        // Les relations (patient, cadreAdministratif) doivent être gérées par le service
//        // car nous avons besoin des repositories pour charger les entités
//
//        return facture;
//    }
//
//    @Override
//    public Facture toEntity(FactureCreationDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        Facture facture = new Facture();
//        facture.setDate(dto.getDate());
//        facture.setMontantTotal(dto.getMontantTotal());
//        facture.setStatutPaiement(dto.getStatutPaiement());
//        facture.setMethodePaiement(dto.getMethodePaiement());
//
//        // Les relations (patient, cadreAdministratif, services) doivent être gérées par le service
//        // car nous avons besoin des repositories pour charger les entités
//
//        return facture;
//    }
//
//    @Override
//    public FactureDetailDTO toDetailDTO(Facture facture) {
//        if (facture == null) {
//            return null;
//        }
//
//        // D'abord, créer un FactureDTO de base
//        FactureDTO baseDto = toDTO(facture);
//
//        // Ensuite, créer le FactureDetailDTO en étendant le FactureDTO
//        FactureDetailDTO detailDto = new FactureDetailDTO();
//
//        // Copier toutes les propriétés de base
//        detailDto.setId(baseDto.getId());
//        detailDto.setNumero(baseDto.getNumero());
//        detailDto.setDate(baseDto.getDate());
//        detailDto.setMontantTotal(baseDto.getMontantTotal());
//        detailDto.setStatutPaiement(baseDto.getStatutPaiement());
//        detailDto.setMethodePaiement(baseDto.getMethodePaiement());
//        detailDto.setPatientId(baseDto.getPatientId());
//        detailDto.setPatientNom(baseDto.getPatientNom());
//        detailDto.setPatientPrenom(baseDto.getPatientPrenom());
//        detailDto.setCadreAdministratifId(baseDto.getCadreAdministratifId());
//        detailDto.setCadreAdministratifNom(baseDto.getCadreAdministratifNom());
//
//        // Ajouter les propriétés spécifiques à FactureDetailDTO
//
//        // Pour les services, nous devons créer une liste de ServiceDTO
//        if (facture.getServiceHospitaliers() != null) {
//            List<ServiceDTO> serviceDTOs = new ArrayList<>();
//            for (ServiceHospitalier service : facture.getServiceHospitaliers()) {
//                ServiceDTO serviceDTO = new ServiceDTO();
//                serviceDTO.setId(service.getId());
//                serviceDTO.setNom(service.getNom());
//                serviceDTO.setDescription(service.getDescription());
//                serviceDTO.setTarif(service.getTarif());
//                serviceDTOs.add(serviceDTO);
//            }
//            detailDto.setServiceDTOS(serviceDTOs);
//        }
//
//        // Pour le patient, nous devons créer un PatientDTO
//        if (facture.getPatient() != null) {
//            Patient patient = facture.getPatient();
//            PatientDTO patientDTO = new PatientDTO();
//            patientDTO.setId(patient.getId());
//            patientDTO.setNom(patient.getNom());
//            patientDTO.setPrenom(patient.getPrenom());
//            // Ajoutez d'autres propriétés selon votre modèle
//            detailDto.setPatientDTO(patientDTO);
//        }
//
//        // Pour le cadre administratif, nous devons créer un CadreAdministratifDTO
//        if (facture.getCadreAdministratif() != null) {
//            CadreAdministratif cadre = facture.getCadreAdministratif();
//            CadreAdministratifDTO cadreDTO = new CadreAdministratifDTO();
//            cadreDTO.setId(cadre.getId());
//            cadreDTO.setNom(cadre.getNom());
//            cadreDTO.setPrenom(cadre.getPrenom());
//            cadreDTO.setFonction(cadre.getFonction());
//            // Ajoutez d'autres propriétés selon votre modèle
//            detailDto.setCadreAdministratifDTO(cadreDTO);
//        }
//
//        return detailDto;
//    }
//}