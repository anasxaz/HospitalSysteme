//package com.hospital.HospitalSysteme.mapper.impl;
//
//import com.hospital.HospitalSysteme.dto.PlanDeSoinsDTO;
//import com.hospital.HospitalSysteme.dto.PlanDeSoinsCreationDTO;
//import com.hospital.HospitalSysteme.dto.PlanDeSoinsUpdateDTO;
//import com.hospital.HospitalSysteme.entity.Infirmier;
//import com.hospital.HospitalSysteme.entity.Patient;
//import com.hospital.HospitalSysteme.entity.PlanDeSoins;
//import com.hospital.HospitalSysteme.entity.enums.StatutPlanDeSoins;
//import com.hospital.HospitalSysteme.mapper.InfirmierMapper;
//import com.hospital.HospitalSysteme.mapper.PatientMapper;
//import com.hospital.HospitalSysteme.mapper.PlanDeSoinsMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PlanDeSoinsMapperImpl implements PlanDeSoinsMapper {
//
//    private final PatientMapper patientMapper;
//    private final InfirmierMapper infirmierMapper;
//
//    @Autowired
//    public PlanDeSoinsMapperImpl(PatientMapper patientMapper, InfirmierMapper infirmierMapper) {
//        this.patientMapper = patientMapper;
//        this.infirmierMapper = infirmierMapper;
//    }
//
//    @Override
//    public PlanDeSoinsDTO toDTO(PlanDeSoins planDeSoins) {
//        if (planDeSoins == null) {
//            return null;
//        }
//
//        PlanDeSoinsDTO planDeSoinsDTO = new PlanDeSoinsDTO();
//
//        planDeSoinsDTO.setId(planDeSoins.getId());
//        planDeSoinsDTO.setDateDebut(planDeSoins.getDateDebut());
//        planDeSoinsDTO.setDateFin(planDeSoins.getDateFin());
//        planDeSoinsDTO.setDescription(planDeSoins.getDescription());
//        planDeSoinsDTO.setInstructions(planDeSoins.getInstructions());
//        planDeSoinsDTO.setStatut(planDeSoins.getStatut());
//
//        if (planDeSoins.getPatient() != null) {
//            planDeSoinsDTO.setPatientId(planDeSoins.getPatient().getId());
//            planDeSoinsDTO.setPatientNom(planDeSoins.getPatient().getNom());
//            planDeSoinsDTO.setPatientPrenom(planDeSoins.getPatient().getPrenom());
//        }
//
//        if (planDeSoins.getInfirmier() != null) {
//            planDeSoinsDTO.setInfirmierId(planDeSoins.getInfirmier().getId());
//            planDeSoinsDTO.setInfirmierNom(planDeSoins.getInfirmier().getNom());
//            planDeSoinsDTO.setInfirmierPrenom(planDeSoins.getInfirmier().getPrenom());
//        }
//
//        return planDeSoinsDTO;
//    }
//
//    @Override
//    public PlanDeSoins toEntity(PlanDeSoinsDTO planDeSoinsDTO) {
//        if (planDeSoinsDTO == null) {
//            return null;
//        }
//
//        PlanDeSoins planDeSoins = new PlanDeSoins();
//
//        planDeSoins.setId(planDeSoinsDTO.getId());
//        planDeSoins.setDateDebut(planDeSoinsDTO.getDateDebut());
//        planDeSoins.setDateFin(planDeSoinsDTO.getDateFin());
//        planDeSoins.setDescription(planDeSoinsDTO.getDescription());
//        planDeSoins.setInstructions(planDeSoinsDTO.getInstructions());
//        planDeSoins.setStatut(planDeSoinsDTO.getStatut());
//
//        if (planDeSoinsDTO.getPatientId() != null) {
//            Patient patient = new Patient();
//            patient.setId(planDeSoinsDTO.getPatientId());
//            planDeSoins.setPatient(patient);
//        }
//
//        if (planDeSoinsDTO.getInfirmierId() != null) {
//            Infirmier infirmier = new Infirmier();
//            infirmier.setId(planDeSoinsDTO.getInfirmierId());
//            planDeSoins.setInfirmier(infirmier);
//        }
//
//        return planDeSoins;
//    }
//
//    @Override
//    public PlanDeSoins toEntity(PlanDeSoinsCreationDTO planDeSoinsCreationDTO) {
//        if (planDeSoinsCreationDTO == null) {
//            return null;
//        }
//
//        PlanDeSoins planDeSoins = new PlanDeSoins();
//
//        planDeSoins.setDateDebut(planDeSoinsCreationDTO.getDateDebut());
//        planDeSoins.setDateFin(planDeSoinsCreationDTO.getDateFin());
//        planDeSoins.setDescription(planDeSoinsCreationDTO.getDescription());
//        planDeSoins.setInstructions(planDeSoinsCreationDTO.getInstructions());
//        planDeSoins.setStatut(StatutPlanDeSoins.EN_COURS);
//
//        if (planDeSoinsCreationDTO.getPatientId() != null) {
//            Patient patient = new Patient();
//            patient.setId(planDeSoinsCreationDTO.getPatientId());
//            planDeSoins.setPatient(patient);
//        }
//
//        if (planDeSoinsCreationDTO.getInfirmierId() != null) {
//            Infirmier infirmier = new Infirmier();
//            infirmier.setId(planDeSoinsCreationDTO.getInfirmierId());
//            planDeSoins.setInfirmier(infirmier);
//        }
//
//        return planDeSoins;
//    }
//
//    @Override
//    public void updatePlanDeSoinsFromDTO(PlanDeSoinsUpdateDTO dto, PlanDeSoins planDeSoins) {
//        if (dto == null || planDeSoins == null) {
//            return;
//        }
//
//        if (dto.getDateDebut() != null) {
//            planDeSoins.setDateDebut(dto.getDateDebut());
//        }
//
//        if (dto.getDateFin() != null) {
//            planDeSoins.setDateFin(dto.getDateFin());
//        }
//
//        if (dto.getDescription() != null) {
//            planDeSoins.setDescription(dto.getDescription());
//        }
//
//        if (dto.getInstructions() != null) {
//            planDeSoins.setInstructions(dto.getInstructions());
//        }
//
//        if (dto.getStatut() != null) {
//            planDeSoins.setStatut(dto.getStatut());
//        }
//    }
//}