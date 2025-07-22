package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.NotificationCreationDTO;
import com.hospital.HospitalSysteme.dto.NotificationDTO;
import com.hospital.HospitalSysteme.dto.NotificationUpdateDTO;
import com.hospital.HospitalSysteme.entity.Notification;
import com.hospital.HospitalSysteme.entity.BasicUser;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {BasicUser.class})
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.nom", target = "userNom")
    @Mapping(source = "user.prenom", target = "userPrenom")
    NotificationDTO toDTO(Notification notification);

    List<NotificationDTO> toDTOList(List<Notification> notifications);

//    @Mapping(source = "userId", target = "user.id")
//    @Mapping(target = "user", expression = "java(new BasicUser())")
    Notification toEntity(NotificationCreationDTO notificationCreationDTO);

    void updateNotificationFromDTO(NotificationUpdateDTO notificationUpdateDTO, @MappingTarget Notification notification);
}