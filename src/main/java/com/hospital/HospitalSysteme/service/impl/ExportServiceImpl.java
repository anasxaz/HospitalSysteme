package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.export.ExportOptionsDTO;
import com.hospital.HospitalSysteme.dto.export.ExportResultDTO;
import com.hospital.HospitalSysteme.entity.*;
import com.hospital.HospitalSysteme.entity.enums.GroupeSanguin;
import com.hospital.HospitalSysteme.entity.enums.StatutPaiement;
import com.hospital.HospitalSysteme.enums.ExportFormat;
import com.hospital.HospitalSysteme.enums.ExportType;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.DashboardService;
import com.hospital.HospitalSysteme.service.ExportService;
import com.hospital.HospitalSysteme.util.export.CSVExporter;
import com.hospital.HospitalSysteme.util.export.ExcelExporter;
import com.hospital.HospitalSysteme.util.export.PDFExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportServiceImpl implements ExportService {

    private final PatientRepository patientRepository;
    private final RendezVousRepository rendezVousRepository;
    private final ConsultationRepository consultationRepository;
    private final FactureRepository factureRepository;
    private final MedecinRepository medecinRepository;
    private final MedicamentRepository medicamentRepository;
    private final PlanningRepository planningRepository;
    private final PersonnelRepository personnelRepository;
    private final DashboardService dashboardService;

    // Exporters
    private final PDFExporter pdfExporter;
    private final ExcelExporter excelExporter;
    private final CSVExporter csvExporter;

    @Override
    public ExportResultDTO exportData(ExportOptionsDTO options) {
        log.info("Exportation des données de type {} au format {}", options.getType(), options.getFormat());

        try {
            switch (options.getType()) {
                case PATIENTS:
                    return exportPatients(options.getFormat(), options.getFiltresSupplementaires());
                case RENDEZ_VOUS:
                    return exportRendezVous(options.getFormat(), options.getDateDebut(),
                            options.getDateFin(), options.getEntityId());
                case CONSULTATIONS:
                    return exportConsultations(options.getFormat(), options.getDateDebut(),
                            options.getDateFin(), options.getEntityId());
                case FACTURES:
                    return exportFactures(options.getFormat(), options.getDateDebut(),
                            options.getDateFin(), options.getStatut());
                case STATISTIQUES:
                    return exportStatistiques(options.getFormat(), options.getStatut());
                case MEDICAMENTS:
                    return exportMedicaments(options.getFormat(), options.getFiltresSupplementaires());
                case PLANNING:
                    return exportPlanning(options.getFormat(), options.getDateDebut(),
                            options.getDateFin(), options.getEntityId());
                case PERSONNEL:
                    return exportPersonnel(options.getFormat(), options.getFiltresSupplementaires());
                default:
                    throw new IllegalArgumentException("Type d'exportation non supporté: " + options.getType());
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'exportation des données", e);
            ExportResultDTO errorResult = new ExportResultDTO();
            errorResult.setSuccess(false);
            errorResult.setMessage("Erreur lors de l'exportation: " + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public ExportResultDTO exportPatients(ExportFormat format, Map<String, Object> filtres) {
        log.info("Exportation des patients avec filtres: {}", filtres);

        // Récupérer les données des patients
        List<Patient> patients;
        if (filtres != null && !filtres.isEmpty()) {
            String nom = (String) filtres.getOrDefault("nom", null);
            String prenom = (String) filtres.getOrDefault("prenom", null);
            String groupeSanguin = (String) filtres.getOrDefault("groupeSanguin", null);
            patients = patientRepository.findForExport(nom, prenom, groupeSanguin);
        } else {
            patients = patientRepository.findAll();
        }

        // Préparer les en-têtes
        List<String> headers = Arrays.asList("ID", "Nom", "Prénom", "Email", "Téléphone",
                "Date de naissance", "Groupe sanguin", "Allergies", "Adresse");

        // Préparer les données
        List<List<String>> data = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Patient patient : patients) {
            List<String> row = new ArrayList<>();
            row.add(patient.getId().toString());
            row.add(patient.getNom());
            row.add(patient.getPrenom());
            row.add(patient.getEmail() != null ? patient.getEmail() : "");
            row.add(patient.getTelephone() != null ? patient.getTelephone() : "");
            row.add(patient.getDateNaissance() != null ?
                    patient.getDateNaissance().format(dateFormatter) : "");
            row.add(patient.getGroupeSanguin() != null ?
                    patient.getGroupeSanguin().toString() : "");
            row.add(patient.getAllergies() != null ? patient.getAllergies() : "");
            row.add(patient.getAdresse() != null ? patient.getAdresse() : "");
            data.add(row);
        }

        // Métadonnées pour le document
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des patients");
        metadata.put("subject", "Patients enregistrés dans le système hospitalier");
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        return generateExportFile(format, "Liste des patients", "patients", headers, data, metadata);
    }

    // AJOUTER cette nouvelle méthode spécialisée
    public ExportResultDTO exportPatientsByGroupeSanguin(ExportFormat format, String groupeSanguinString) {
        log.info("Exportation des patients par groupe sanguin: {}", groupeSanguinString);

        List<Patient> patients;
        if (groupeSanguinString != null && !groupeSanguinString.isEmpty()) {
            // Convertir String vers enum GroupeSanguin
            GroupeSanguin groupeSanguin = convertStringToGroupeSanguin(groupeSanguinString);

            if (groupeSanguin != null) {
                // Utiliser une méthode repository spécifique pour le groupe sanguin
                patients = patientRepository.findByGroupeSanguin(groupeSanguin);
            } else {
                // Si conversion échoue, récupérer tous les patients
                patients = patientRepository.findAll();
            }
        } else {
            // Si pas de filtre, récupérer tous les patients
            patients = patientRepository.findAll();
        }

        // Préparer les en-têtes
        List<String> headers = Arrays.asList("ID", "Nom", "Prénom", "Email", "Téléphone",
                "Date de naissance", "Groupe sanguin", "Allergies", "Adresse");

        // Préparer les données
        List<List<String>> data = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Patient patient : patients) {
            List<String> row = new ArrayList<>();
            row.add(patient.getId().toString());
            row.add(patient.getNom());
            row.add(patient.getPrenom());
            row.add(patient.getEmail() != null ? patient.getEmail() : "");
            row.add(patient.getTelephone() != null ? patient.getTelephone() : "");
            row.add(patient.getDateNaissance() != null ?
                    patient.getDateNaissance().format(dateFormatter) : "");
            row.add(patient.getGroupeSanguin() != null ?
                    patient.getGroupeSanguin().toString() : "");
            row.add(patient.getAllergies() != null ? patient.getAllergies() : "");
            row.add(patient.getAdresse() != null ? patient.getAdresse() : "");
            data.add(row);
        }

        // Métadonnées pour le document
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des patients par groupe sanguin");
        metadata.put("subject", groupeSanguinString != null ?
                "Patients du groupe sanguin " + groupeSanguinString :
                "Tous les patients");
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        String title = "Liste des patients";
        if (groupeSanguinString != null && !groupeSanguinString.isEmpty()) {
            title += " - Groupe sanguin " + groupeSanguinString;
        }

        return generateExportFile(format, title, "patients_groupe_sanguin", headers, data, metadata);
    }

    // AJOUTER cette méthode utilitaire de conversion
    private GroupeSanguin convertStringToGroupeSanguin(String groupeSanguinString) {
        if (groupeSanguinString == null || groupeSanguinString.isEmpty()) {
            return null;
        }

        try {
            // Adapter selon le format de votre enum GroupeSanguin
            switch (groupeSanguinString.toUpperCase()) {
                case "A+": return GroupeSanguin.A_PLUS;  // ou GroupeSanguin.valueOf("A_PLUS")
                case "A-": return GroupeSanguin.A_MOINS;
                case "B+": return GroupeSanguin.B_PLUS;
                case "B-": return GroupeSanguin.B_MOINS;
                case "AB+": return GroupeSanguin.AB_PLUS;
                case "AB-": return GroupeSanguin.AB_MOINS;
                case "O+": return GroupeSanguin.O_PLUS;
                case "O-": return GroupeSanguin.O_MOINS;
                default:
                    log.warn("Groupe sanguin non reconnu: {}", groupeSanguinString);
                    return null;
            }
        } catch (Exception e) {
            log.warn("Erreur lors de la conversion du groupe sanguin: {}. Erreur: {}",
                    groupeSanguinString, e.getMessage());
            return null;
        }
    }

//    @Override
//    public ExportResultDTO exportRendezVous(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long medecinId) {
//        log.info("Exportation des rendez-vous du {} au {} pour le médecin ID: {}", dateDebut, dateFin, medecinId);
//
//        // Convertir les dates en LocalDateTime pour la requête
////        LocalDateTime debutDateTime = dateDebut != null ? dateDebut.atStartOfDay() : LocalDateTime.now().minusMonths(1);
////        LocalDateTime finDateTime = dateFin != null ? dateFin.atTime(LocalTime.MAX) : LocalDateTime.now().plusMonths(1);
//
//        // Récupérer les rendez-vous
////        List<RendezVous> rendezVousList;
////        if (medecinId != null) {
////            // Remplacer par la méthode disponible dans votre repository
////            rendezVousList = rendezVousRepository.findByMedecinIdAndDateHeureBetween(medecinId, debutDateTime, finDateTime);
////            // Si cette méthode n'existe pas, vous pouvez utiliser:
////            // rendezVousList = rendezVousRepository.findAll().stream()
////            //     .filter(rdv -> rdv.getMedecin() != null && rdv.getMedecin().getId().equals(medecinId))
////            //     .filter(rdv -> rdv.getDateHeure().isAfter(debutDateTime) && rdv.getDateHeure().isBefore(finDateTime))
////            //     .collect(Collectors.toList());
////        } else {
////            rendezVousList = rendezVousRepository.findByDateHeureBetween(debutDateTime, finDateTime);
////        }
//        List<RendezVous> rendezVousList;
//        if (dateDebut != null && dateFin != null) {
//            // Si dates spécifiées, les utiliser
//            LocalDateTime debutDateTime = dateDebut.atStartOfDay();
//            LocalDateTime finDateTime = dateFin.atTime(LocalTime.MAX);
//
//            if (medecinId != null) {
//                rendezVousList = rendezVousRepository.findByMedecinIdAndDateHeureBetween(medecinId, debutDateTime, finDateTime);
//            } else {
//                rendezVousList = rendezVousRepository.findByDateHeureBetween(debutDateTime, finDateTime);
//            }
//        } else {
//            // NOUVEAU : Si pas de dates, récupérer TOUS les rendez-vous
//            if (medecinId != null) {
//                // Supposons que vous avez cette méthode dans votre repository
//                rendezVousList = rendezVousRepository.findByMedecinId(medecinId);
//            } else {
//                rendezVousList = rendezVousRepository.findAll();
//            }
//        }
//
//        // Préparer les en-têtes
//        List<String> headers = Arrays.asList("ID", "Date et heure", "Patient", "Médecin", "Statut", "Motif", "Notes");
//
//        // Préparer les données
//        List<List<String>> data = new ArrayList<>();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//
//        for (RendezVous rdv : rendezVousList) {
//            List<String> row = new ArrayList<>();
//            row.add(rdv.getId().toString());
//            row.add(rdv.getDateHeure().format(dateTimeFormatter));
//
//            // Vérifier si le patient existe et a des propriétés nom et prénom
//            if (rdv.getPatient() != null) {
//                String patientNom = rdv.getPatient().getNom() != null ? rdv.getPatient().getNom() : "";
//                String patientPrenom = rdv.getPatient().getPrenom() != null ? rdv.getPatient().getPrenom() : "";
//                row.add(patientNom + " " + patientPrenom);
//            } else {
//                row.add("");
//            }
//
//            // Vérifier si le médecin existe et a des propriétés nom et prénom
//            if (rdv.getMedecin() != null) {
//                String medecinNom = rdv.getMedecin().getNom() != null ? rdv.getMedecin().getNom() : "";
//                String medecinPrenom = rdv.getMedecin().getPrenom() != null ? rdv.getMedecin().getPrenom() : "";
//                row.add(medecinNom + " " + medecinPrenom);
//            } else {
//                row.add("");
//            }
//
//            // Vérifier si le statut existe
//            row.add(rdv.getStatut() != null ? rdv.getStatut().name() : "");
//
//            // Ajouter motif et notes avec vérification null
//            row.add(rdv.getMotif() != null ? rdv.getMotif() : "");
//            row.add(rdv.getNotes() != null ? rdv.getNotes() : "");
//
//            data.add(row);
//        }
//
//        // Métadonnées pour le document
//        Map<String, Object> metadata = new HashMap<>();
//        metadata.put("title", "Liste des rendez-vous");
//        metadata.put("subject", "Rendez-vous du " + dateDebut + " au " + dateFin);
//        metadata.put("creator", "Système Hospitalier");
//        metadata.put("author", "Service d'exportation");
//        metadata.put("creationDate", LocalDateTime.now());
//
//        String title = "Liste des rendez-vous";
//        if (medecinId != null) {
//            Medecin medecin = medecinRepository.findById(medecinId).orElse(null);
//            if (medecin != null) {
//                title += " du Dr. " + medecin.getNom() + " " + medecin.getPrenom();
//            }
//        }
//        title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
//                " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//
//        return generateExportFile(format, title, "rendez_vous", headers, data, metadata);
//    }


    // 1. CORRECTION RENDEZ_VOUS - Nom de méthode incorrect
    @Override
    public ExportResultDTO exportRendezVous(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long medecinId) {
        log.info("Exportation des rendez-vous du {} au {} pour le médecin ID: {}", dateDebut, dateFin, medecinId);

        List<RendezVous> rendezVousList;
        if (dateDebut != null && dateFin != null) {
            // Convertir les dates en LocalDateTime pour la requête
            LocalDateTime debutDateTime = dateDebut.atStartOfDay();
            LocalDateTime finDateTime = dateFin.atTime(LocalTime.MAX);

            if (medecinId != null) {
                // CORRECTION: Utiliser le bon nom de méthode de votre repository
                rendezVousList = rendezVousRepository.findByMedecinIdAndDateHeureBetween(medecinId, debutDateTime, finDateTime);
            } else {
                rendezVousList = rendezVousRepository.findByDateHeureBetween(debutDateTime, finDateTime);
            }
        } else {
            // Si pas de dates, récupérer tous les rendez-vous
            if (medecinId != null) {
                rendezVousList = rendezVousRepository.findByMedecinId(medecinId);
            } else {
                rendezVousList = rendezVousRepository.findAll();
            }
        }

        // Préparer les en-têtes
        List<String> headers = Arrays.asList("ID", "Date et heure", "Patient", "Médecin", "Statut", "Motif", "Notes");

        // Préparer les données
        List<List<String>> data = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (RendezVous rdv : rendezVousList) {
            List<String> row = new ArrayList<>();
            row.add(rdv.getId().toString());
            row.add(rdv.getDateHeure().format(dateTimeFormatter));

            // Patient
            if (rdv.getPatient() != null) {
                String patientNom = rdv.getPatient().getNom() != null ? rdv.getPatient().getNom() : "";
                String patientPrenom = rdv.getPatient().getPrenom() != null ? rdv.getPatient().getPrenom() : "";
                row.add(patientNom + " " + patientPrenom);
            } else {
                row.add("");
            }

            // Médecin
            if (rdv.getMedecin() != null) {
                String medecinNom = rdv.getMedecin().getNom() != null ? rdv.getMedecin().getNom() : "";
                String medecinPrenom = rdv.getMedecin().getPrenom() != null ? rdv.getMedecin().getPrenom() : "";
                row.add(medecinNom + " " + medecinPrenom);
            } else {
                row.add("");
            }

            row.add(rdv.getStatut() != null ? rdv.getStatut().name() : "");
            row.add(rdv.getMotif() != null ? rdv.getMotif() : "");
            row.add(rdv.getNotes() != null ? rdv.getNotes() : "");

            data.add(row);
        }

        // Métadonnées
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des rendez-vous");
        metadata.put("subject", dateDebut != null && dateFin != null ?
                "Rendez-vous du " + dateDebut + " au " + dateFin :
                "Tous les rendez-vous");
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        String title = "Liste des rendez-vous";
        if (medecinId != null) {
            Medecin medecin = medecinRepository.findById(medecinId).orElse(null);
            if (medecin != null) {
                title += " du Dr. " + medecin.getNom() + " " + medecin.getPrenom();
            }
        }

        if (dateDebut != null && dateFin != null) {
            title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            title += " (tous)";
        }

        return generateExportFile(format, title, "rendez_vous", headers, data, metadata);
    }















//    @Override
//    public ExportResultDTO exportConsultations(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long medecinId) {
//        log.info("Exportation des consultations du {} au {} pour le médecin ID: {}", dateDebut, dateFin, medecinId);
//
//        // Convertir les dates en LocalDateTime pour la requête
//        LocalDateTime debutDateTime = dateDebut != null ? dateDebut.atStartOfDay() : LocalDateTime.now().minusMonths(1);
//        LocalDateTime finDateTime = dateFin != null ? dateFin.atTime(LocalTime.MAX) : LocalDateTime.now().plusMonths(1);
//
//        // Récupérer les consultations
//        List<Consultation> consultations;
//        if (medecinId != null) {
//            // Remplacer par la méthode disponible dans votre repository
//            consultations = consultationRepository.findByMedecinIdAndDateBetween(medecinId, debutDateTime, finDateTime);
//            // Si cette méthode n'existe pas, vous pouvez utiliser:
//            // consultations = consultationRepository.findAll().stream()
//            //     .filter(c -> c.getMedecin() != null && c.getMedecin().getId().equals(medecinId))
//            //     .filter(c -> c.getDate().isAfter(debutDateTime) && c.getDate().isBefore(finDateTime))
//            //     .collect(Collectors.toList());
//        } else {
//            consultations = consultationRepository.findByDateBetween(debutDateTime, finDateTime);
//        }
//
//        // Préparer les en-têtes - adapter selon les propriétés disponibles dans votre entité Consultation
//        List<String> headers = Arrays.asList("ID", "Date", "Patient", "Médecin", "Diagnostic", "Notes");
//
//        // Préparer les données
//        List<List<String>> data = new ArrayList<>();
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        for (Consultation consultation : consultations) {
//            List<String> row = new ArrayList<>();
//            row.add(consultation.getId().toString());
//            row.add(consultation.getDate().format(dateFormatter));
//
//            // Vérifier si le patient existe et a des propriétés nom et prénom
//            if (consultation.getDossierMedical().getPatient() != null) {
//                String patientNom = consultation.getDossierMedical().getPatient().getNom() != null ? consultation.getDossierMedical().getPatient().getNom() : "";
//                String patientPrenom = consultation.getDossierMedical().getPatient().getPrenom() != null ? consultation.getDossierMedical().getPatient().getPrenom() : "";
//                row.add(patientNom + " " + patientPrenom);
//            } else {
//                row.add("");
//            }
//
//            // Vérifier si le médecin existe et a des propriétés nom et prénom
//            if (consultation.getMedecin() != null) {
//                String medecinNom = consultation.getMedecin().getNom() != null ? consultation.getMedecin().getNom() : "";
//                String medecinPrenom = consultation.getMedecin().getPrenom() != null ? consultation.getMedecin().getPrenom() : "";
//                row.add(medecinNom + " " + medecinPrenom);
//            } else {
//                row.add("");
//            }
//
//            // Ajouter diagnostic et notes avec vérification null
//            row.add(consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "");
//
//            // Si votre entité Consultation n'a pas de propriété traitement, supprimez cette ligne
//            // row.add(consultation.getTraitement() != null ? consultation.getTraitement() : "");
//
//            row.add(consultation.getNotes() != null ? consultation.getNotes() : "");
//
//            data.add(row);
//        }
//
//        // Métadonnées pour le document
//        Map<String, Object> metadata = new HashMap<>();
//        metadata.put("title", "Liste des consultations");
//        metadata.put("subject", "Consultations du " + dateDebut + " au " + dateFin);
//        metadata.put("creator", "Système Hospitalier");
//        metadata.put("author", "Service d'exportation");
//        metadata.put("creationDate", LocalDateTime.now());
//
//        String title = "Liste des consultations";
//        if (medecinId != null) {
//            Medecin medecin = medecinRepository.findById(medecinId).orElse(null);
//            if (medecin != null) {
//                title += " du Dr. " + medecin.getNom() + " " + medecin.getPrenom();
//            }
//        }
//        title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
//                " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//
//        return generateExportFile(format, title, "consultations", headers, data, metadata);
//    }

    // 1. FIX pour CONSULTATIONS - Récupérer TOUTES les consultations si pas de dates
    @Override
    public ExportResultDTO exportConsultations(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long medecinId) {
        log.info("Exportation des consultations du {} au {} pour le médecin ID: {}", dateDebut, dateFin, medecinId);

        // CORRECTION : Récupérer toutes les consultations si pas de filtre de date
        List<Consultation> consultations;
        if (dateDebut != null && dateFin != null) {
            // Si dates spécifiées, les utiliser
            LocalDateTime debutDateTime = dateDebut.atStartOfDay();
            LocalDateTime finDateTime = dateFin.atTime(LocalTime.MAX);

            if (medecinId != null) {
                consultations = consultationRepository.findByMedecinIdAndDateBetween(medecinId, debutDateTime, finDateTime);
            } else {
                consultations = consultationRepository.findByDateBetween(debutDateTime, finDateTime);
            }
        } else {
            // NOUVEAU : Si pas de dates, récupérer TOUTES les consultations
            if (medecinId != null) {
                // Supposons que vous avez cette méthode dans votre repository
                consultations = consultationRepository.findByMedecinId(medecinId);
            } else {
                consultations = consultationRepository.findAll();
            }
        }

        // Reste du code identique...
        List<String> headers = Arrays.asList("ID", "Date", "Patient", "Médecin", "Diagnostic", "Notes");
        List<List<String>> data = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Consultation consultation : consultations) {
            List<String> row = new ArrayList<>();
            row.add(consultation.getId().toString());
            row.add(consultation.getDate().format(dateFormatter));

            if (consultation.getDossierMedical().getPatient() != null) {
                String patientNom = consultation.getDossierMedical().getPatient().getNom() != null ? consultation.getDossierMedical().getPatient().getNom() : "";
                String patientPrenom = consultation.getDossierMedical().getPatient().getPrenom() != null ? consultation.getDossierMedical().getPatient().getPrenom() : "";
                row.add(patientNom + " " + patientPrenom);
            } else {
                row.add("");
            }

            if (consultation.getMedecin() != null) {
                String medecinNom = consultation.getMedecin().getNom() != null ? consultation.getMedecin().getNom() : "";
                String medecinPrenom = consultation.getMedecin().getPrenom() != null ? consultation.getMedecin().getPrenom() : "";
                row.add(medecinNom + " " + medecinPrenom);
            } else {
                row.add("");
            }

            row.add(consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "");
            row.add(consultation.getNotes() != null ? consultation.getNotes() : "");
            data.add(row);
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des consultations");
        metadata.put("subject", dateDebut != null && dateFin != null ?
                "Consultations du " + dateDebut + " au " + dateFin :
                "Toutes les consultations");
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        String title = "Liste des consultations";
        if (medecinId != null) {
            Medecin medecin = medecinRepository.findById(medecinId).orElse(null);
            if (medecin != null) {
                title += " du Dr. " + medecin.getNom() + " " + medecin.getPrenom();
            }
        }

        if (dateDebut != null && dateFin != null) {
            title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            title += " (toutes)";
        }

        return generateExportFile(format, title, "consultations", headers, data, metadata);
    }



    @Override
    public ExportResultDTO exportFactures(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, String statut) {
        log.info("Exportation des factures du {} au {} avec statut: {}", dateDebut, dateFin, statut);

        // Convertir les dates en LocalDateTime pour la requête
        LocalDateTime debutDateTime = dateDebut != null ? dateDebut.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime finDateTime = dateFin != null ? dateFin.atTime(LocalTime.MAX) : LocalDateTime.now().plusMonths(1);

        // Récupérer les factures
        List<Facture> factures;
        if (statut != null && !statut.isEmpty()) {
            try {
                // Convertir la chaîne en enum StatutPaiement
                StatutPaiement statutPaiement = StatutPaiement.valueOf(statut);
                factures = factureRepository.findByDateBetweenAndStatutPaiement(debutDateTime, finDateTime, statutPaiement);
            } catch (IllegalArgumentException e) {
                log.warn("Statut invalide: {}. Récupération de toutes les factures.", statut);
                factures = factureRepository.findByDateBetween(debutDateTime, finDateTime);
            }
        } else {
            factures = factureRepository.findByDateBetween(debutDateTime, finDateTime);
        }

        // Préparer les en-têtes - adapter selon les propriétés disponibles dans votre entité Facture
        List<String> headers = Arrays.asList("ID", "Date", "Patient", "Montant total", "Montant payé",
                "Reste à payer", "Statut");

        // Préparer les données
        List<List<String>> data = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Facture facture : factures) {
            List<String> row = new ArrayList<>();
            row.add(facture.getId().toString());
            row.add(facture.getDate().format(dateFormatter));

            // Vérifier si le patient existe et a des propriétés nom et prénom
            if (facture.getPatient() != null) {
                String patientNom = facture.getPatient().getNom() != null ? facture.getPatient().getNom() : "";
                String patientPrenom = facture.getPatient().getPrenom() != null ? facture.getPatient().getPrenom() : "";
                row.add(patientNom + " " + patientPrenom);
            } else {
                row.add("");
            }

            // Montant total
            row.add(facture.getMontantTotal() != null ? facture.getMontantTotal().toString() + " DH" : "0 DH");

            // Montant payé - si votre entité n'a pas cette propriété, adaptez ou supprimez
            BigDecimal montantPaye = facture.getMontantTotal();
            row.add(montantPaye != null ? montantPaye.toString() + " DH" : "0 DH");

            // Calcul du reste à payer - adapter selon les propriétés disponibles
            BigDecimal montantTotal = facture.getMontantTotal() != null ? facture.getMontantTotal() : BigDecimal.ZERO;
            BigDecimal restant = montantTotal.subtract(montantPaye != null ? montantPaye : BigDecimal.ZERO);
            row.add(restant.toString() + " DH");

            // Statut - adapter selon le type de votre propriété statut
            if (facture.getStatutPaiement() != null) {
                // Si statut est un enum
                row.add(facture.getStatutPaiement().name());
                // Si statut est une chaîne
                // row.add(facture.getStatut());
            } else {
                row.add("");
            }

            // Si votre entité Facture n'a pas ces propriétés, supprimez ces lignes
            // row.add(facture.getModePaiement() != null ? facture.getModePaiement().toString() : "");
            // row.add(facture.getService() != null ? facture.getService() : "");

            data.add(row);
        }

        // Métadonnées pour le document
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des factures");
        metadata.put("subject", "Factures du " + dateDebut + " au " + dateFin);
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        String title = "Liste des factures";
        if (statut != null && !statut.isEmpty()) {
            title += " avec statut " + statut;
        }
        title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return generateExportFile(format, title, "factures", headers, data, metadata);
    }

    @Override
    public ExportResultDTO exportStatistiques(ExportFormat format, String periode) {
        log.info("Exportation des statistiques pour la période: {}", periode);

        // Récupérer les statistiques depuis le service de tableau de bord
        var stats = dashboardService.getGlobalStats();

        // Préparer les en-têtes et données selon le type de statistiques
        List<String> headers = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        // Statistiques générales
        headers.add("Indicateur");
        headers.add("Valeur");

        // Ajouter les données de base
        addStatRow(data, "Nombre total de patients", String.valueOf(stats.getTotalPatients()));
        addStatRow(data, "Nombre total de médecins", String.valueOf(stats.getTotalMedecins()));
        addStatRow(data, "Nombre total d'infirmiers", String.valueOf(stats.getTotalInfirmiers()));
        addStatRow(data, "Nombre total de rendez-vous", String.valueOf(stats.getTotalRendezVous()));
        addStatRow(data, "Rendez-vous aujourd'hui", String.valueOf(stats.getRendezVousAujourdhui()));
        addStatRow(data, "Consultations aujourd'hui", String.valueOf(stats.getConsultationsAujourdhui()));
        addStatRow(data, "Revenus journaliers", stats.getRevenusJournaliers() + " DH");

        // Ajouter les données de répartition
        addStatRow(data, "--- Répartition des rendez-vous par statut ---", "");
        if (stats.getRendezVousParStatut() != null) {
            for (Map.Entry<String, Long> entry : stats.getRendezVousParStatut().entrySet()) {
                addStatRow(data, "Rendez-vous " + entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        addStatRow(data, "--- Répartition des patients par groupe sanguin ---", "");
        if (stats.getPatientsParGroupeSanguin() != null) {
            for (Map.Entry<String, Long> entry : stats.getPatientsParGroupeSanguin().entrySet()) {
                addStatRow(data, "Groupe " + entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        addStatRow(data, "--- Consultations par mois ---", "");
        if (stats.getConsultationsParMois() != null) {
            for (Map.Entry<String, Long> entry : stats.getConsultationsParMois().entrySet()) {
                addStatRow(data, entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        // Métadonnées pour le document
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Statistiques du système hospitalier");
        metadata.put("subject", "Statistiques générales");
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        String title = "Statistiques du système hospitalier";
        if (periode != null && !periode.isEmpty()) {
            title += " - Période: " + periode;
        }

        return generateExportFile(format, title, "statistiques", headers, data, metadata);
    }

    // Méthode utilitaire pour ajouter une ligne de statistique
    private void addStatRow(List<List<String>> data, String indicateur, String valeur) {
        List<String> row = new ArrayList<>();
        row.add(indicateur);
        row.add(valeur);
        data.add(row);
    }

    // Méthode pour exporter les médicaments
//    private ExportResultDTO exportMedicaments(ExportFormat format, Map<String, Object> filtres) {
//        log.info("Exportation des médicaments avec filtres: {}", filtres);
//
//        // Cette méthode serait implémentée de manière similaire aux autres méthodes d'exportation
//        // en utilisant le repository approprié pour récupérer les données des médicaments
//
//        // Pour l'exemple, nous allons créer des données fictives
//        List<String> headers = Arrays.asList("ID", "Nom", "Description", "Dosage", "Forme", "Prix", "Stock");
//        List<List<String>> data = new ArrayList<>();
//
//        // Données fictives pour l'exemple
//        List<String> row1 = Arrays.asList("1", "Paracétamol", "Analgésique et antipyrétique", "500mg", "Comprimé", "15.50 DH", "250");
//        List<String> row2 = Arrays.asList("2", "Amoxicilline", "Antibiotique", "1g", "Gélule", "45.00 DH", "120");
//        List<String> row3 = Arrays.asList("3", "Ibuprofène", "Anti-inflammatoire", "400mg", "Comprimé", "22.75 DH", "180");
//
//        data.add(row1);
//        data.add(row2);
//        data.add(row3);
//
//        Map<String, Object> metadata = new HashMap<>();
//        metadata.put("title", "Liste des médicaments");
//
//        return generateExportFile(format, "Liste des médicaments", "medicaments", headers, data, metadata);
//    }

    // 2. CORRECTION MEDICAMENTS - Utiliser vraies données
    private ExportResultDTO exportMedicaments(ExportFormat format, Map<String, Object> filtres) {
        log.info("Exportation des médicaments avec filtres: {}", filtres);

        // Récupérer les vraies données de médicaments
        List<Medicament> medicaments;
        if (filtres != null && !filtres.isEmpty()) {
            String nom = (String) filtres.getOrDefault("nom", null);
            String categorie = (String) filtres.getOrDefault("categorie", null);
            String fabricant = (String) filtres.getOrDefault("fabricant", null);

            if (nom != null && !nom.isEmpty()) {
                medicaments = medicamentRepository.findByNomContainingIgnoreCase(nom);
            } else if (categorie != null && !categorie.isEmpty()) {
                medicaments = medicamentRepository.findByCategorie(categorie);
            } else if (fabricant != null && !fabricant.isEmpty()) {
                medicaments = medicamentRepository.findByFabricant(fabricant);
            } else {
                medicaments = medicamentRepository.findAll();
            }
        } else {
            medicaments = medicamentRepository.findAll();
        }

        // Préparer les en-têtes selon votre entité Medicament
        List<String> headers = Arrays.asList("ID", "Nom", "Description", "Dosage", "Catégorie",
                "Fabricant", "Ordonnance requise", "Effets secondaires");

        // Préparer les données avec vos vraies propriétés
        List<List<String>> data = new ArrayList<>();
        for (Medicament medicament : medicaments) {
            List<String> row = new ArrayList<>();
            row.add(medicament.getId().toString());
            row.add(medicament.getNom() != null ? medicament.getNom() : "");
            row.add(medicament.getDescription() != null ? medicament.getDescription() : "");
            row.add(medicament.getDosage() != null ? medicament.getDosage() : "");
            row.add(medicament.getCategorie() != null ? medicament.getCategorie() : "");
            row.add(medicament.getFabricant() != null ? medicament.getFabricant() : "");
            row.add(medicament.getOrdonnanceRequise() != null ?
                    (medicament.getOrdonnanceRequise() ? "Oui" : "Non") : "Non");
            row.add(medicament.getEffetsSecondaires() != null ? medicament.getEffetsSecondaires() : "");
            data.add(row);
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des médicaments");
        metadata.put("subject", "Inventaire des médicaments du système hospitalier");
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        return generateExportFile(format, "Liste des médicaments", "medicaments", headers, data, metadata);
    }




    // Méthode pour exporter le planning
//    private ExportResultDTO exportPlanning(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long departementId) {
//        log.info("Exportation du planning du {} au {} pour le département ID: {}", dateDebut, dateFin, departementId);
//
//        // Cette méthode serait implémentée de manière similaire aux autres méthodes d'exportation
//        // en utilisant le repository approprié pour récupérer les données du planning
//
//        // Pour l'exemple, nous allons créer des données fictives
//        List<String> headers = Arrays.asList("ID", "Date", "Heure début", "Heure fin", "Médecin", "Département", "Statut");
//        List<List<String>> data = new ArrayList<>();
//
//        // Données fictives pour l'exemple
//        List<String> row1 = Arrays.asList("1", "01/07/2025", "08:00", "12:00", "Dr. Martin", "Cardiologie", "ACTIF");
//        List<String> row2 = Arrays.asList("2", "01/07/2025", "14:00", "18:00", "Dr. Dupont", "Cardiologie", "ACTIF");
//        List<String> row3 = Arrays.asList("3", "02/07/2025", "09:00", "13:00", "Dr. Martin", "Cardiologie", "ACTIF");
//
//        data.add(row1);
//        data.add(row2);
//        data.add(row3);
//
//        Map<String, Object> metadata = new HashMap<>();
//        metadata.put("title", "Planning des médecins");
//
//        String title = "Planning";
//        if (departementId != null) {
//            title += " du département ID: " + departementId;
//        }
//        title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
//                " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//
//        return generateExportFile(format, title, "planning", headers, data, metadata);
//    }

    // 3. CORRECTION PLANNING - Utiliser vraies données
    private ExportResultDTO exportPlanning(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long departementId) {
        log.info("Exportation du planning du {} au {} pour le département ID: {}", dateDebut, dateFin, departementId);

        // Dates par défaut si null
        if (dateDebut == null) {
            dateDebut = LocalDate.now().withDayOfMonth(1);
        }
        if (dateFin == null) {
            dateFin = LocalDate.now();
        }

        // Récupérer TOUS les plannings pour commencer
        List<Planning> plannings = planningRepository.findAll();

        // En-têtes
        List<String> headers = Arrays.asList("ID", "Titre", "Description", "Date début",
                "Date fin", "Statut");

        // Données
        List<List<String>> data = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Planning planning : plannings) {
            List<String> row = new ArrayList<>();
            row.add(planning.getId().toString());
            row.add(planning.getTitre() != null ? planning.getTitre() : "");
            row.add(planning.getDescription() != null ? planning.getDescription() : "");
            row.add(planning.getDateDebut() != null ? planning.getDateDebut().format(dateFormatter) : "");
            row.add(planning.getDateFin() != null ? planning.getDateFin().format(dateFormatter) : "");
            row.add(planning.getStatut() != null ? planning.getStatut().name() : "");
            data.add(row);
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Planning hospitalier");
        metadata.put("subject", "Planning du " + dateDebut + " au " + dateFin);
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        String title = "Planning hospitalier";
        if (departementId != null) {
            title += " du département ID: " + departementId;
        }
        title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return generateExportFile(format, title, "planning", headers, data, metadata);
    }



    // Méthode pour exporter le personnel
//    private ExportResultDTO exportPersonnel(ExportFormat format, Map<String, Object> filtres) {
//        log.info("Exportation du personnel avec filtres: {}", filtres);
//
//        // Cette méthode serait implémentée de manière similaire aux autres méthodes d'exportation
//        // en utilisant le repository approprié pour récupérer les données du personnel
//
//        // Pour l'exemple, nous allons créer des données fictives
//        List<String> headers = Arrays.asList("ID", "Nom", "Prénom", "Fonction", "Département", "Email", "Téléphone");
//        List<List<String>> data = new ArrayList<>();
//
//        // Données fictives pour l'exemple
//        List<String> row1 = Arrays.asList("1", "Martin", "Jean", "Médecin", "Cardiologie", "jean.martin@hospital.com", "0612345678");
//        List<String> row2 = Arrays.asList("2", "Dupont", "Marie", "Infirmière", "Cardiologie", "marie.dupont@hospital.com", "0623456789");
//        List<String> row3 = Arrays.asList("3", "Durand", "Pierre", "Médecin", "Pédiatrie", "pierre.durand@hospital.com", "0634567890");
//
//        data.add(row1);
//        data.add(row2);
//        data.add(row3);
//
//        Map<String, Object> metadata = new HashMap<>();
//        metadata.put("title", "Liste du personnel");
//
//        return generateExportFile(format, "Liste du personnel", "personnel", headers, data, metadata);
//    }

    // 4. CORRECTION PERSONNEL - Utiliser vraies données
    private ExportResultDTO exportPersonnel(ExportFormat format, Map<String, Object> filtres) {
        log.info("Exportation du personnel avec filtres: {}", filtres);

        // Récupérer les vraies données de personnel
        List<Personnel> personnels;
        if (filtres != null && !filtres.isEmpty()) {
            // Pour l'instant, récupérer tous - vous pouvez ajouter des filtres spécifiques
            personnels = personnelRepository.findAll();

            // Exemple de filtrage manuel si nécessaire
            String poste = (String) filtres.getOrDefault("poste", null);
            if (poste != null && !poste.isEmpty()) {
                personnels = personnels.stream()
                        .filter(p -> p.getPoste() != null && p.getPoste().toLowerCase().contains(poste.toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else {
            personnels = personnelRepository.findAll();
        }

        // En-têtes selon votre entité Personnel
        List<String> headers = Arrays.asList("ID", "Nom", "Prénom", "Email", "Téléphone",
                "Poste", "Département", "Date embauche", "Salaire");

        // Données avec vos vraies propriétés
        List<List<String>> data = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Personnel personnel : personnels) {
            List<String> row = new ArrayList<>();
            row.add(personnel.getId().toString());
            row.add(personnel.getNom() != null ? personnel.getNom() : "");
            row.add(personnel.getPrenom() != null ? personnel.getPrenom() : "");
            row.add(personnel.getEmail() != null ? personnel.getEmail() : "");
            row.add(personnel.getTelephone() != null ? personnel.getTelephone() : "");
            row.add(personnel.getPoste() != null ? personnel.getPoste() : "");
            row.add(personnel.getDepartement() != null ? personnel.getDepartement().getNom() : "");
            row.add(personnel.getDateEmbauche() != null ?
                    personnel.getDateEmbauche().format(dateFormatter) : "");
            row.add(personnel.getSalaire() != null ?
                    personnel.getSalaire().toString() + " DH" : "0 DH");
            data.add(row);
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste du personnel");
        metadata.put("subject", "Personnel du système hospitalier");
        metadata.put("creator", "Système Hospitalier");
        metadata.put("author", "Service d'exportation");
        metadata.put("creationDate", LocalDateTime.now());

        return generateExportFile(format, "Liste du personnel", "personnel", headers, data, metadata);
    }



    // Méthode utilitaire pour générer le fichier d'exportation selon le format demandé
    private ExportResultDTO generateExportFile(ExportFormat format, String title, String filePrefix,
                                               List<String> headers, List<List<String>> data,
                                               Map<String, Object> metadata) {
        String fileName = filePrefix + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        byte[] fileContent;
        String fileType;

        switch (format) {
            case PDF:
                fileContent = pdfExporter.exportToPdf(title, headers, data, metadata);
                fileType = "application/pdf";
                fileName += ".pdf";
                break;
            case EXCEL:
                fileContent = excelExporter.exportToExcel(filePrefix, headers, data, metadata);
                fileType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName += ".xlsx";
                break;
            case CSV:
                fileContent = csvExporter.exportToCsv(headers, data);
                fileType = "text/csv";
                fileName += ".csv";
                break;
            case DOCX:
                // Implémentation pour DOCX si nécessaire
                throw new UnsupportedOperationException("Format DOCX non encore implémenté");
            default:
                throw new IllegalArgumentException("Format non supporté: " + format);
        }

        // Créer et retourner le résultat
        ExportResultDTO result = new ExportResultDTO();
        result.setFileName(fileName);
        result.setFileType(fileType);
        result.setFileContent(fileContent);
        result.setFileSize(fileContent.length);
        result.setSuccess(true);
        result.setMessage("Exportation réussie");

        // Générer une URL de téléchargement (fictive pour l'exemple)
        result.setDownloadUrl("/api/export/download/" + UUID.randomUUID().toString());

        log.info("Exportation réussie: {} ({} octets)", fileName, fileContent.length);

        return result;
    }
}