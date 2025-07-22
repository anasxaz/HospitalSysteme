package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.ServiceCreationDTO;
import com.hospital.HospitalSysteme.dto.ServiceDTO;
import com.hospital.HospitalSysteme.dto.ServiceUpdateDTO;
import com.hospital.HospitalSysteme.entity.ServiceHospitalier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceDTO toDTO(ServiceHospitalier serviceHospitalier);

    List<ServiceDTO> toDTOList(List<ServiceHospitalier> serviceHospitaliers);

    ServiceHospitalier toEntity(ServiceCreationDTO serviceCreationDTO);

    void updateServiceFromDTO(ServiceUpdateDTO serviceUpdateDTO, @MappingTarget ServiceHospitalier serviceHospitalier);
}