package com.hospital.HospitalSysteme.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminDTO extends UserDTO {
    private LocalDateTime derniereConnexionAdmin;
}
