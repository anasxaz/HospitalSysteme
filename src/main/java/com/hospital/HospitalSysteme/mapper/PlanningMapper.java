package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.PlanningCreationDTO;
import com.hospital.HospitalSysteme.dto.PlanningDTO;
import com.hospital.HospitalSysteme.dto.PlanningUpdateDTO;
import com.hospital.HospitalSysteme.entity.CadreAdministratif;
import com.hospital.HospitalSysteme.entity.Planning;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PlanningMapper {

    PlanningMapper INSTANCE = Mappers.getMapper(PlanningMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "cadreAdministratifs", ignore = true)
    Planning toEntity(PlanningCreationDTO planningCreationDTO);

    @Mapping(target = "cadreAdministratifIds", expression = "java(mapCadreAdministratifsToIds(planning.getCadreAdministratifs()))")
    // Si vous avez accès au département via les cadres administratifs:
    @Mapping(target = "departementId", expression = "java(getDepartementId(planning.getCadreAdministratifs()))")
    @Mapping(target = "departementNom", expression = "java(getDepartementNom(planning.getCadreAdministratifs()))")
    PlanningDTO toDTO(Planning planning);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cadreAdministratifs", ignore = true)
    void updateEntityFromDTO(PlanningUpdateDTO planningUpdateDTO, @MappingTarget Planning planning);

    // Méthodes par défaut pour les conversions personnalisées
    default Set<Long> mapCadreAdministratifsToIds(Set<CadreAdministratif> cadreAdministratifs) {
        if (cadreAdministratifs == null) {
            return null;
        }
        return cadreAdministratifs.stream()
                .map(CadreAdministratif::getId)
                .collect(Collectors.toSet());
    }

    // Ces méthodes supposent que tous les cadres administratifs d'un planning
    // appartiennent au même département. Si ce n'est pas le cas, vous devrez
    // adapter cette logique.
    default Long getDepartementId(Set<CadreAdministratif> cadreAdministratifs) {
        if (cadreAdministratifs == null || cadreAdministratifs.isEmpty()) {
            return null;
        }
        // Prendre le premier cadre administratif et récupérer son département
        return cadreAdministratifs.iterator().next().getDepartement().getId();
    }

    default String getDepartementNom(Set<CadreAdministratif> cadreAdministratifs) {
        if (cadreAdministratifs == null || cadreAdministratifs.isEmpty()) {
            return null;
        }
        // Prendre le premier cadre administratif et récupérer le nom de son département
        return cadreAdministratifs.iterator().next().getDepartement().getNom();
    }
}