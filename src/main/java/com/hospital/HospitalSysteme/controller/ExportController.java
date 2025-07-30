package com.hospital.HospitalSysteme.controller;

import java.util.stream.Collectors;
import com.hospital.HospitalSysteme.dto.export.ExportOptionsDTO;
import com.hospital.HospitalSysteme.dto.export.ExportResultDTO;
import com.hospital.HospitalSysteme.enums.ExportFormat;
import com.hospital.HospitalSysteme.enums.ExportType;
import com.hospital.HospitalSysteme.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Export", description = "API pour l'exportation de données hospitalières en différents formats (PDF, Excel, CSV)")
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API d'exportation")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans ExportController fonctionne");
    }

    // ====================== EXPORTATION GÉNÉRIQUE ======================

//    @PostMapping("/data")
//    @Operation(summary = "Exporter des données selon les options",
//            description = "Exporte des données hospitalières selon les options spécifiées (type, format, filtres)")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Exportation réussie",
//                    content = @Content(schema = @Schema(implementation = ExportResultDTO.class))),
//            @ApiResponse(responseCode = "400", description = "Options d'exportation invalides"),
//            @ApiResponse(responseCode = "403", description = "Accès refusé"),
//            @ApiResponse(responseCode = "500", description = "Erreur lors de l'exportation")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
//    public ResponseEntity<ExportResultDTO> exportData(
//            @Parameter(description = "Options d'exportation détaillées")
//            @Valid @RequestBody ExportOptionsDTO options) {
//        log.info("Demande d'exportation de données - Type: {}, Format: {}", options.getType(), options.getFormat());
//
//        ExportResultDTO result = exportService.exportData(options);
//
//        if (result.isSuccess()) {
//            return ResponseEntity.ok(result);
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
//        }
//    }

//    @PostMapping("/download")
//    @Operation(summary = "Télécharger directement un fichier exporté",
//            description = "Génère et télécharge directement un fichier d'exportation selon les options")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Fichier téléchargé avec succès"),
//            @ApiResponse(responseCode = "400", description = "Options d'exportation invalides"),
//            @ApiResponse(responseCode = "403", description = "Accès refusé"),
//            @ApiResponse(responseCode = "500", description = "Erreur lors de l'exportation")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
//    public ResponseEntity<byte[]> downloadExport(
//            @Parameter(description = "Options d'exportation détaillées")
//            @Valid @RequestBody ExportOptionsDTO options) {
//        log.info("Demande de téléchargement direct - Type: {}, Format: {}", options.getType(), options.getFormat());
//
//        ExportResultDTO result = exportService.exportData(options);
//
//        if (result.isSuccess()) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.parseMediaType(result.getFileType()));
//            headers.setContentDispositionFormData("attachment", result.getFileName());
//            headers.setContentLength(result.getFileSize());
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(result.getFileContent());
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    // ====================== EXPORTATIONS SPÉCIALISÉES ======================

    @GetMapping("/patients")
    @Operation(summary = "Exporter la liste des patients",
            description = "Exporte la liste des patients avec filtres optionnels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export patients réussi"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<byte[]> exportPatients(
            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
            @RequestParam(defaultValue = "PDF") ExportFormat format,

            @Parameter(
                    description = "Groupe sanguin (filtrage optionnel)",
                    example = "A+",
                    schema = @Schema(
                            type = "string",
                            allowableValues = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"}
                    )
            )
            @RequestParam(required = false) String groupeSanguin) {

        log.info("Exportation des patients - Format: {}, Groupe sanguin: {}", format, groupeSanguin);

        // UTILISER la nouvelle méthode spécialisée
        ExportResultDTO result = exportService.exportPatientsByGroupeSanguin(format, groupeSanguin);
        return createDownloadResponse(result);
    }
//    public ResponseEntity<byte[]> exportPatients(
//            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
//            @RequestParam(defaultValue = "PDF") ExportFormat format,
//
//            @Parameter(
//                    description = "Groupe sanguin (filtrage optionnel)",
//                    example = "A+",
//                    schema = @Schema(
//                            type = "string",
//                            allowableValues = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"}
//                    )
//            )
//            @RequestParam(required = false) String groupeSanguin) {
//
//        log.info("Exportation des patients - Format: {}, Groupe sanguin: {}", format, groupeSanguin);
//
//        Map<String, Object> filtres = new HashMap<>();
//        if (groupeSanguin != null && !groupeSanguin.isEmpty()) {
//            filtres.put("groupeSanguin", groupeSanguin);
//        }
//
//        ExportResultDTO result = exportService.exportPatients(format, filtres);
//        return createDownloadResponse(result);
//    }




//    public ResponseEntity<byte[]> exportPatients(
//            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
//            @RequestParam(defaultValue = "PDF") ExportFormat format,
//            @Parameter(description = "Nom du patient (filtrage)")
//            @RequestParam(required = false) String nom,
//            @Parameter(description = "Prénom du patient (filtrage)")
//            @RequestParam(required = false) String prenom,
//            @Parameter(description = "Groupe sanguin (filtrage)")
//            @RequestParam(required = false) String groupeSanguin) {
//        log.info("Exportation des patients - Format: {}, Filtres: nom={}, prenom={}, groupeSanguin={}",
//                format, nom, prenom, groupeSanguin);
//
//        Map<String, Object> filtres = new HashMap<>();
//        if (nom != null) filtres.put("nom", nom);
//        if (prenom != null) filtres.put("prenom", prenom);
//        if (groupeSanguin != null) filtres.put("groupeSanguin", groupeSanguin);
//
//        ExportResultDTO result = exportService.exportPatients(format, filtres);
//
//        return createDownloadResponse(result);
//    }

    @GetMapping("/rendez-vous")
    @Operation(summary = "Exporter les rendez-vous",
            description = "Exporte les rendez-vous pour une période et optionnellement un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export rendez-vous réussi"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<byte[]> exportRendezVous(
            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
            @RequestParam(defaultValue = "PDF") ExportFormat format,
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @Parameter(description = "ID du médecin (filtrage optionnel)")
            @RequestParam(required = false) Long medecinId) {
        log.info("Exportation des rendez-vous - Format: {}, Période: {} à {}, Médecin: {}",
                format, dateDebut, dateFin, medecinId);

        ExportResultDTO result = exportService.exportRendezVous(format, dateDebut, dateFin, medecinId);

        return createDownloadResponse(result);
    }

    @GetMapping("/consultations")
    @Operation(summary = "Exporter les consultations",
            description = "Exporte les consultations pour une période et optionnellement un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export consultations réussi"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<byte[]> exportConsultations(
            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
            @RequestParam(defaultValue = "PDF") ExportFormat format,
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @Parameter(description = "ID du médecin (filtrage optionnel)")
            @RequestParam(required = false) Long medecinId) {
        log.info("Exportation des consultations - Format: {}, Période: {} à {}, Médecin: {}",
                format, dateDebut, dateFin, medecinId);

        ExportResultDTO result = exportService.exportConsultations(format, dateDebut, dateFin, medecinId);

        return createDownloadResponse(result);
    }

    @GetMapping("/factures")
    @Operation(summary = "Exporter les factures",
            description = "Exporte les factures pour une période avec filtrage par statut optionnel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export factures réussi"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<byte[]> exportFactures(
            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
            @RequestParam(defaultValue = "PDF") ExportFormat format,

            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,

            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,

            @Parameter(
                    description = "Statut de paiement (filtrage optionnel)",
                    example = "EN_ATTENTE",
                    schema = @Schema(
                            type = "string",
                            allowableValues = {"EN_ATTENTE", "PAYEE", "ANNULEE"}
                    )
            )
            @RequestParam(required = false) String statut) {

        log.info("Exportation des factures - Format: {}, Période: {} à {}, Statut: {}",
                format, dateDebut, dateFin, statut);

        ExportResultDTO result = exportService.exportFactures(format, dateDebut, dateFin, statut);
        return createDownloadResponse(result);
    }
//    public ResponseEntity<byte[]> exportFactures(
//            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
//            @RequestParam(defaultValue = "PDF") ExportFormat format,
//            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
//            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
//            @Parameter(description = "Statut de paiement (EN_ATTENTE, PAYEE, ANNULEE)")
//            @RequestParam(required = false) String statut) {
//        log.info("Exportation des factures - Format: {}, Période: {} à {}, Statut: {}",
//                format, dateDebut, dateFin, statut);
//
//        ExportResultDTO result = exportService.exportFactures(format, dateDebut, dateFin, statut);
//
//        return createDownloadResponse(result);
//    }

    @GetMapping("/statistiques")
    @Operation(summary = "Exporter les statistiques",
            description = "Exporte un rapport complet des statistiques hospitalières")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export statistiques réussi"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<byte[]> exportStatistiques(
            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
            @RequestParam(defaultValue = "PDF") ExportFormat format,
            @Parameter(description = "Période d'analyse (optionnel)")
            @RequestParam(required = false) String periode) {
        log.info("Exportation des statistiques - Format: {}, Période: {}", format, periode);

        ExportResultDTO result = exportService.exportStatistiques(format, periode);

        return createDownloadResponse(result);
    }

    // ====================== EXPORTS RAPIDES PAR TYPE ======================

    @GetMapping("/quick/{type}")
    @Operation(summary = "Export rapide par type",
            description = "Génère rapidement un export standard pour un type de données donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export rapide réussi"),
            @ApiResponse(responseCode = "400", description = "Type d'export invalide"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<byte[]> quickExport(@PathVariable ExportType type, @RequestParam(defaultValue = "PDF") ExportFormat format) {
        log.info("Export rapide - Type: {}, Format: {}", type, format);

        ExportOptionsDTO options = new ExportOptionsDTO();
        options.setType(type);
        options.setFormat(format);

        // CORRECTION: Ajouter PLANNING dans le switch
        switch (type) {
            case RENDEZ_VOUS:
            case CONSULTATIONS:
            case FACTURES:
            case PLANNING:  // ← AJOUTER CETTE LIGNE
                options.setDateDebut(LocalDate.now().withDayOfMonth(1));
                options.setDateFin(LocalDate.now());
                break;
            default:
                // Pour PATIENTS, MEDICAMENTS, PERSONNEL, STATISTIQUES - pas de dates
                break;
        }

        ExportResultDTO result = exportService.exportData(options);
        return createDownloadResponse(result);
    }
//    public ResponseEntity<byte[]> quickExport(
//            @Parameter(description = "Type d'exportation (PATIENTS, RENDEZ_VOUS, CONSULTATIONS, etc.)")
//            @PathVariable ExportType type,
//            @Parameter(description = "Format d'exportation (PDF, EXCEL, CSV)")
//            @RequestParam(defaultValue = "PDF") ExportFormat format) {
//        log.info("Export rapide - Type: {}, Format: {}", type, format);
//
//        ExportOptionsDTO options = new ExportOptionsDTO();
//        options.setType(type);
//        options.setFormat(format);
//
//        // Définir des paramètres par défaut selon le type
//        switch (type) {
//            case RENDEZ_VOUS:
//            case CONSULTATIONS:
//            case FACTURES:
//                // Pour les données temporelles, prendre le mois courant
//                options.setDateDebut(LocalDate.now().withDayOfMonth(1));
//                options.setDateFin(LocalDate.now());
//                break;
////            case PLANNING:  // ← Ajoutez cette ligne
////                options.setDateDebut(LocalDate.now().withDayOfMonth(1));
////                options.setDateFin(LocalDate.now());
////                break;
//            default:
//                // Pour les autres types, pas de filtre de date
//                break;
//        }
//
//        ExportResultDTO result = exportService.exportData(options);
//
//        return createDownloadResponse(result);
//    }

    // ====================== EXPORTS PERSONNALISÉS ======================

//    @PostMapping("/custom")
//    @Operation(summary = "Export personnalisé avec options avancées",
//            description = "Crée un export personnalisé avec des options de formatage et filtrage avancées")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Export personnalisé réussi"),
//            @ApiResponse(responseCode = "400", description = "Options invalides"),
//            @ApiResponse(responseCode = "403", description = "Accès refusé")
//    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
//    public ResponseEntity<byte[]> customExport(
//            @Parameter(description = "Options d'exportation personnalisées")
//            @Valid @RequestBody ExportOptionsDTO options) {
//        log.info("Export personnalisé - Type: {}, Format: {}, Filtres: {}",
//                options.getType(), options.getFormat(), options.getFiltresSupplementaires());
//
//        ExportResultDTO result = exportService.exportData(options);
//
//        return createDownloadResponse(result);
//    }

    // ====================== INFORMATIONS SUR LES EXPORTS ======================

    @GetMapping("/formats")
    @Operation(summary = "Récupérer les formats d'export disponibles",
            description = "Retourne la liste des formats d'exportation supportés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des formats récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<Map<String, Object>> getAvailableFormats() {
        log.info("Demande de liste des formats d'exportation disponibles");

        Map<String, Object> formats = new HashMap<>();
        formats.put("PDF", Map.of(
                "description", "Portable Document Format",
                "extension", ".pdf",
                "mimeType", "application/pdf",
                "recommande", "Documents officiels et rapports"
        ));
        formats.put("EXCEL", Map.of(
                "description", "Microsoft Excel Workbook",
                "extension", ".xlsx",
                "mimeType", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "recommande", "Analyse de données et tableaux"
        ));
        formats.put("CSV", Map.of(
                "description", "Comma Separated Values",
                "extension", ".csv",
                "mimeType", "text/csv",
                "recommande", "Import dans autres systèmes"
        ));

        return ResponseEntity.ok(Map.of(
                "formats", formats,
                "defaultFormat", "PDF",
                "note", "Choisissez le format selon votre usage"
        ));
    }

    @GetMapping("/types")
    @Operation(summary = "Récupérer les types d'export disponibles",
            description = "Retourne la liste des types de données exportables")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des types récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN', 'INFIRMIER')")
    public ResponseEntity<Map<String, Object>> getAvailableTypes() {
        log.info("Demande de liste des types d'exportation disponibles");

        Map<String, Object> types = new HashMap<>();
        types.put("PATIENTS", "Liste des patients avec informations personnelles");
        types.put("RENDEZ_VOUS", "Planning des rendez-vous médicaux");
        types.put("CONSULTATIONS", "Historique des consultations");
        types.put("FACTURES", "État de la facturation et paiements");
        types.put("STATISTIQUES", "Rapport statistique global");
        types.put("MEDICAMENTS", "Inventaire des médicaments");
        types.put("PLANNING", "Planning du personnel médical");
        types.put("PERSONNEL", "Liste du personnel hospitalier");

        return ResponseEntity.ok(Map.of(
                "types", types,
                "note", "Chaque type a ses propres options de filtrage"
        ));
    }

    // ====================== MÉTHODES UTILITAIRES ======================

    private ResponseEntity<byte[]> createDownloadResponse(ExportResultDTO result) {
        if (result.isSuccess()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(result.getFileType()));
            headers.setContentDispositionFormData("attachment", result.getFileName());
            headers.setContentLength(result.getFileSize());
            headers.add("X-Export-Status", "SUCCESS");
            headers.add("X-Export-Message", result.getMessage());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(result.getFileContent());
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Export-Status", "ERROR");
            headers.add("X-Export-Message", result.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .build();
        }
    }
}