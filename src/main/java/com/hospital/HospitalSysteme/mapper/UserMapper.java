package com.hospital.HospitalSysteme.mapper;

import com.hospital.HospitalSysteme.dto.UserDTO;
import com.hospital.HospitalSysteme.dto.UserUpdateDTO;
import com.hospital.HospitalSysteme.entity.BasicUser;
import com.hospital.HospitalSysteme.entity.User;
import org.mapstruct.*;
import com.hospital.HospitalSysteme.dto.UserDTO;



@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDTO toDTO(User user);
//    @BeanMapping(resultType = com.hospital.HospitalSysteme.entity.Personnel.class)

    @BeanMapping(resultType = BasicUser.class)
    User toEntity(UserDTO userDTO);

    void updateUserFromDTO(UserUpdateDTO dto, @MappingTarget User user);

}
