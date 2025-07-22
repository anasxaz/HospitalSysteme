package com.hospital.HospitalSysteme.dto.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportResultDTO {
    private String fileName;
    private String fileType;
    private byte[] fileContent;
    private long fileSize;
    private String message;
    private boolean success;
    private String downloadUrl; // URL pour télécharger le fichier
}