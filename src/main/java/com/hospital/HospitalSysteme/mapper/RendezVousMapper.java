package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.RendezVousCreationDTO;
import com.hospital.HospitalSysteme.dto.RendezVousDTO;
import com.hospital.HospitalSysteme.dto.RendezVousUpdateDTO;
import com.hospital.HospitalSysteme.entity.Medecin;
import com.hospital.HospitalSysteme.entity.Patient;
import com.hospital.HospitalSysteme.entity.RendezVous;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.repository.MedecinRepository;
import com.hospital.HospitalSysteme.repository.PatientRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {MedecinRepository.class, PatientRepository.class})
public interface RendezVousMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientNom", source = "patient.nom")
    @Mapping(target = "patientPrenom", source = "patient.prenom")
    @Mapping(target = "medecinId", source = "medecin.id")
    @Mapping(target = "medecinNom", source = "medecin.nom")
    @Mapping(target = "medecinPrenom", source = "medecin.prenom")
    @Mapping(target = "medecinSpecialite", source = "medecin.specialite")
    RendezVousDTO toDTO(RendezVous rendezVous);


    RendezVous toEntity(RendezVousDTO rendezVousDTO);

    // Ne pas mapper medecin et patient directement
    @Mapping(target = "medecin", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "statut", constant = "PROGRAMME")
    RendezVous toEntity(RendezVousCreationDTO rendezVousCreationDTO);

    void updateRendezVousFromDTO(RendezVousUpdateDTO rendezVousUpdateDTO, @MappingTarget RendezVous rendezVous);

}