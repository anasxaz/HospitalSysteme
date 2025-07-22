package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.entity.DossierMedical;
import com.hospital.HospitalSysteme.entity.RendezVous;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RendezVousMapper {

    RendezVousDTO toDTO(RendezVous rendezVous);
    //    PatientDetailDTO toDetailDTO(Patient patient);
    RendezVous toEntity(RendezVousDTO rendezVousDTO);
    RendezVous toEntity(RendezVousCreationDTO rendezVousCreationDTO);
    void updateRendezVousFromDTO(RendezVousUpdateDTO rendezVousUpdateDTO, @MappingTarget RendezVous rendezVous);


}
