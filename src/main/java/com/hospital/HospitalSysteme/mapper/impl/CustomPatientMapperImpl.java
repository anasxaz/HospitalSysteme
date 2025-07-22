//package com.hospital.HospitalSysteme.mapper.impl;
//
//import com.hospital.HospitalSysteme.dto.*;
//import com.hospital.HospitalSysteme.entity.Patient;
//import com.hospital.HospitalSysteme.entity.enums.ProfilUser;
//import com.hospital.HospitalSysteme.mapper.PatientMapper;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
//@Component("customPatientMapperImpl")
//@Primary
//public class CustomPatientMapperImpl implements PatientMapper {
//
//    @Override
//    public PatientDTO toDTO(Patient patient) {
//        if (patient == null) {
//            return null;
//        }
//
//        PatientDTO patientDTO = new PatientDTO();
//
//        // Copier les propriétés de User
//        patientDTO.setId(patient.getId());
//        patientDTO.setNom(patient.getNom());
//        patientDTO.setPrenom(patient.getPrenom());
//        patientDTO.setEmail(patient.getEmail());
//        patientDTO.setTelephone(patient.getTelephone());
//        patientDTO.setAdresse(patient.getAdresse());
//        patientDTO.setDateNaissance(patient.getDateNaissance());
//        patientDTO.setGenre(patient.getGenre());
//        patientDTO.setDateCreation(patient.getDateCreation());
//        patientDTO.setDerniereConnexion(patient.getDerniereConnexion());
//
//        // Copier les propriétés spécifiques à Patient
//        patientDTO.setNumeroAssurance(patient.getNumeroAssurance());
//        patientDTO.setGroupeSanguin(patient.getGroupeSanguin());
//        patientDTO.setAllergies(patient.getAllergies());
//        patientDTO.setContactUrgenceNom(patient.getContactUrgenceNom());
//        patientDTO.setContactUrgenceTelephone(patient.getContactUrgenceTelephone());
//
//        return patientDTO;
//    }
//
//    @Override
//    public PatientDetailDTO toDetailDTO(Patient patient) {
//        if (patient == null) {
//            return null;
//        }
//
//        PatientDetailDTO detailDTO = new PatientDetailDTO();
//
//        // Copier les propriétés de PatientDTO
//        detailDTO.setId(patient.getId());
//        detailDTO.setNom(patient.getNom());
//        detailDTO.setPrenom(patient.getPrenom());
//        detailDTO.setEmail(patient.getEmail());
//        detailDTO.setTelephone(patient.getTelephone());
//        detailDTO.setAdresse(patient.getAdresse());
//        detailDTO.setDateNaissance(patient.getDateNaissance());
//        detailDTO.setGenre(patient.getGenre());
//        detailDTO.setDateCreation(patient.getDateCreation());
//        detailDTO.setDerniereConnexion(patient.getDerniereConnexion());
//
//        detailDTO.setNumeroAssurance(patient.getNumeroAssurance());
//        detailDTO.setGroupeSanguin(patient.getGroupeSanguin());
//        detailDTO.setAllergies(patient.getAllergies());
//        detailDTO.setContactUrgenceNom(patient.getContactUrgenceNom());
//        detailDTO.setContactUrgenceTelephone(patient.getContactUrgenceTelephone());
//
//        // Ajouter les propriétés spécifiques à PatientDetailDTO
//        detailDTO.setAntecedentsMedicaux(patient.getAntecedentsMedicaux());
//
//        // Pour les relations, vous auriez besoin d'autres mappers
//        // Par exemple, pour dossierMedicalDTO, rendezVousDTOS, etc.
//        // Pour l'instant, nous initialisons simplement des listes vides
//        detailDTO.setDossierMedicalDTO(null); // Vous auriez besoin d'un DossierMedicalMapper
//        detailDTO.setRendezVousDTOS(new ArrayList<>()); // Vous auriez besoin d'un RendezVousMapper
//        detailDTO.setFactureDTOS(new ArrayList<>()); // Vous auriez besoin d'un FactureMapper
//        detailDTO.setPlanDeSoinsDTOS(new ArrayList<>()); // Vous auriez besoin d'un PlanDeSoinsMapper
//
//        return detailDTO;
//    }
//
//    @Override
//    public Patient toEntity(PatientDTO patientDTO) {
//        if (patientDTO == null) {
//            return null;
//        }
//
//        Patient patient = new Patient();
//
//        // Copier les propriétés de UserDTO
//        patient.setId(patientDTO.getId());
//        patient.setNom(patientDTO.getNom());
//        patient.setPrenom(patientDTO.getPrenom());
//        patient.setEmail(patientDTO.getEmail());
//        patient.setTelephone(patientDTO.getTelephone());
//        patient.setAdresse(patientDTO.getAdresse());
//        patient.setDateNaissance(patientDTO.getDateNaissance());
//        patient.setGenre(patientDTO.getGenre());
//        // Ne pas copier dateCreation et derniereConnexion car ils sont générés automatiquement
//
//        // Copier les propriétés spécifiques à PatientDTO
//        patient.setNumeroAssurance(patientDTO.getNumeroAssurance());
//        patient.setGroupeSanguin(patientDTO.getGroupeSanguin());
//        patient.setAllergies(patientDTO.getAllergies());
//        patient.setContactUrgenceNom(patientDTO.getContactUrgenceNom());
//        patient.setContactUrgenceTelephone(patientDTO.getContactUrgenceTelephone());
//
//        return patient;
//    }
//
//    @Override
//    public Patient toEntity(PatientCreationDTO patientCreationDTO) {
//        if (patientCreationDTO == null) {
//            return null;
//        }
//
//        Patient patient = new Patient();
//
//        // Copier les propriétés de UserCreationDTO
//        patient.setNom(patientCreationDTO.getNom());
//        patient.setPrenom(patientCreationDTO.getPrenom());
//        patient.setEmail(patientCreationDTO.getEmail());
//        patient.setPassword(patientCreationDTO.getPassword());
//        patient.setTelephone(patientCreationDTO.getTelephone());
//        patient.setAdresse(patientCreationDTO.getAdresse());
//        patient.setDateNaissance(patientCreationDTO.getDateNaissance());
//        patient.setGenre(patientCreationDTO.getGenre());
//        patient.setProfil(ProfilUser.PATIENT); // Définir le profil comme PATIENT
//
//        // Copier les propriétés spécifiques à PatientCreationDTO
//        patient.setNumeroAssurance(patientCreationDTO.getNumeroAssurance());
//        patient.setGroupeSanguin(patientCreationDTO.getGroupeSanguin());
//        patient.setAllergies(patientCreationDTO.getAllergies());
//        patient.setAntecedentsMedicaux(patientCreationDTO.getAntecedentsMedicaux());
//        patient.setContactUrgenceNom(patientCreationDTO.getContactUrgenceNom());
//        patient.setContactUrgenceTelephone(patientCreationDTO.getContactUrgenceTelephone());
//
//        return patient;
//    }
//
//    @Override
//    public void updatePatientFromDTO(PatientUpdateDTO patientUpdateDTO, Patient patient) {
//        if (patientUpdateDTO == null || patient == null) {
//            return;
//        }
//
//        // Mettre à jour les propriétés de User
//        if (patientUpdateDTO.getNom() != null) {
//            patient.setNom(patientUpdateDTO.getNom());
//        }
//        if (patientUpdateDTO.getPrenom() != null) {
//            patient.setPrenom(patientUpdateDTO.getPrenom());
//        }
//        if (patientUpdateDTO.getTelephone() != null) {
//            patient.setTelephone(patientUpdateDTO.getTelephone());
//        }
//        if (patientUpdateDTO.getAdresse() != null) {
//            patient.setAdresse(patientUpdateDTO.getAdresse());
//        }
//        if (patientUpdateDTO.getDateNaissance() != null) {
//            patient.setDateNaissance(patientUpdateDTO.getDateNaissance());
//        }
//        if (patientUpdateDTO.getGenre() != null) {
//            patient.setGenre(patientUpdateDTO.getGenre());
//        }
//
//        // Mettre à jour les propriétés spécifiques à Patient
//        if (patientUpdateDTO.getNumeroAssurance() != null) {
//            patient.setNumeroAssurance(patientUpdateDTO.getNumeroAssurance());
//        }
//        if (patientUpdateDTO.getGroupeSanguin() != null) {
//            patient.setGroupeSanguin(patientUpdateDTO.getGroupeSanguin());
//        }
//        if (patientUpdateDTO.getAllergies() != null) {
//            patient.setAllergies(patientUpdateDTO.getAllergies());
//        }
//        if (patientUpdateDTO.getAntecedentsMedicaux() != null) {
//            patient.setAntecedentsMedicaux(patientUpdateDTO.getAntecedentsMedicaux());
//        }
//        if (patientUpdateDTO.getContactUrgenceNom() != null) {
//            patient.setContactUrgenceNom(patientUpdateDTO.getContactUrgenceNom());
//        }
//        if (patientUpdateDTO.getContactUrgenceTelephone() != null) {
//            patient.setContactUrgenceTelephone(patientUpdateDTO.getContactUrgenceTelephone());
//        }
//    }
//}