package com.hospital.HospitalSysteme.dto.export;

import com.hospital.HospitalSysteme.enums.ExportFormat;
import com.hospital.HospitalSysteme.enums.ExportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportOptionsDTO {

    @NotNull(message = "Le type d'exportation est obligatoire")
    private ExportType type;

    @NotNull(message = "Le format d'exportation est obligatoire")
    private ExportFormat format;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long entityId; // ID du médecin, département, etc.
    private String statut; // Statut des entités à exporter
    private Map<String, Object> filtresSupplementaires; // Filtres additionnels
    private String langue = "fr"; // Langue par défaut
    private boolean inclureEnTete = true; // Inclure l'en-tête dans l'export
    private boolean inclurePiedPage = true; // Inclure le pied de page
}