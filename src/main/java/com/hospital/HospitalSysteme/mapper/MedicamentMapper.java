package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.MedicamentCreationDTO;
import com.hospital.HospitalSysteme.dto.MedicamentDTO;
import com.hospital.HospitalSysteme.dto.MedicamentUpdateDTO;
import com.hospital.HospitalSysteme.entity.Medicament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicamentMapper {

    MedicamentDTO toDTO(Medicament medicament);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    Medicament toEntity(MedicamentCreationDTO medicamentCreationDTO);

    Medicament toEntity(MedicamentDTO medicamentDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    void updateMedicamentFromDTO(MedicamentUpdateDTO medicamentUpdateDTO, @MappingTarget Medicament medicament);
}