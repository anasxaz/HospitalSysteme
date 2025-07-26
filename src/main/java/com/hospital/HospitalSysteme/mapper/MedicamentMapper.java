package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.MedicamentCreationDTO;
import com.hospital.HospitalSysteme.dto.MedicamentDTO;
import com.hospital.HospitalSysteme.dto.MedicamentUpdateDTO;
import com.hospital.HospitalSysteme.entity.Medicament;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicamentMapper {

    // Lecture
    MedicamentDTO toDTO(Medicament medicament);

    // Création
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    Medicament toEntity(MedicamentCreationDTO medicamentCreationDTO);

    // Conversion DTO vers entité
    Medicament toEntity(MedicamentDTO medicamentDTO);

    // Mise à jour - SOLUTION AU PROBLÈME
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    void updateMedicamentFromDTO(MedicamentUpdateDTO medicamentUpdateDTO, @MappingTarget Medicament medicament);


}