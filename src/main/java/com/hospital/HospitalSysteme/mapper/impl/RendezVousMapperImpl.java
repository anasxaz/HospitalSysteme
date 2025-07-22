//package com.hospital.HospitalSysteme.mapper.impl;
//
//import com.hospital.HospitalSysteme.dto.RendezVousCreationDTO;
//import com.hospital.HospitalSysteme.dto.RendezVousDTO;
//import com.hospital.HospitalSysteme.dto.RendezVousUpdateDTO;
//import com.hospital.HospitalSysteme.entity.Medecin;
//import com.hospital.HospitalSysteme.entity.Patient;
//import com.hospital.HospitalSysteme.entity.RendezVous;
//import com.hospital.HospitalSysteme.entity.enums.StatutRendezVous;
//import com.hospital.HospitalSysteme.mapper.RendezVousMapper;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RendezVousMapperImpl implements RendezVousMapper {
//
//    @Override
//    public RendezVousDTO toDTO(RendezVous rendezVous) {
//        if (rendezVous == null) {
//            return null;
//        }
//
//        RendezVousDTO dto = new RendezVousDTO();
//        dto.setId(rendezVous.getId());
//        dto.setDateHeure(rendezVous.getDateHeure());
//        dto.setDuree(rendezVous.getDuree());
//        dto.setStatut(rendezVous.getStatut());
//        dto.setMotif(rendezVous.getMotif());
//        dto.setNotes(rendezVous.getNotes());
//
//        if (rendezVous.getPatient() != null) {
//            Patient patient = rendezVous.getPatient();
//            dto.setPatientId(patient.getId());
//            dto.setPatientNom(patient.getNom());
//            dto.setPatientPrenom(patient.getPrenom());
//        }
//
//        if (rendezVous.getMedecin() != null) {
//            Medecin medecin = rendezVous.getMedecin();
//            dto.setMedecinId(medecin.getId());
//            dto.setMedecinNom(medecin.getNom());
//            dto.setMedecinPrenom(medecin.getPrenom());
//            dto.setMedecinSpecialite(medecin.getSpecialite());
//        }
//
//        return dto;
//    }
//
//    @Override
//    public RendezVous toEntity(RendezVousDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        RendezVous rendezVous = new RendezVous();
//        rendezVous.setId(dto.getId());
//        rendezVous.setDateHeure(dto.getDateHeure());
//        rendezVous.setDuree(dto.getDuree());
//        rendezVous.setStatut(dto.getStatut());
//        rendezVous.setMotif(dto.getMotif());
//        rendezVous.setNotes(dto.getNotes());
//
//        // Les relations (patient, médecin) doivent être gérées par le service
//        // car nous avons besoin des repositories pour charger les entités
//
//        return rendezVous;
//    }
//
//    @Override
//    public RendezVous toEntity(RendezVousCreationDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        RendezVous rendezVous = new RendezVous();
//        rendezVous.setDateHeure(dto.getDateHeure());
//        rendezVous.setDuree(dto.getDuree());
//        rendezVous.setStatut(StatutRendezVous.PROGRAMME); // Valeur par défaut pour un nouveau rendez-vous
//        rendezVous.setMotif(dto.getMotif());
//        rendezVous.setNotes(dto.getNotes());
//
//        // Les relations (patient, médecin) doivent être gérées par le service
//        // car nous avons besoin des repositories pour charger les entités
//
//        return rendezVous;
//    }
//
//    @Override
//    public void updateRendezVousFromDTO(RendezVousUpdateDTO dto, RendezVous rendezVous) {
//        if (dto == null) {
//            return;
//        }
//
//        if (dto.getDateHeure() != null) {
//            rendezVous.setDateHeure(dto.getDateHeure());
//        }
//        if (dto.getDuree() != null) {
//            rendezVous.setDuree(dto.getDuree());
//        }
//        if (dto.getStatut() != null) {
//            rendezVous.setStatut(dto.getStatut());
//        }
//        if (dto.getMotif() != null) {
//            rendezVous.setMotif(dto.getMotif());
//        }
//        if (dto.getNotes() != null) {
//            rendezVous.setNotes(dto.getNotes());
//        }
//    }
//}