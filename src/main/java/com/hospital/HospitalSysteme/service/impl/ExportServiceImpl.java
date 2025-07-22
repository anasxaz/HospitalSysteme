package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.export.ExportOptionsDTO;
import com.hospital.HospitalSysteme.dto.export.ExportResultDTO;
import com.hospital.HospitalSysteme.entity.*;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportServiceImpl implements ExportService {

    private final PatientRepository patientRepository;
    private final RendezVousRepository rendezVousRepository;
    private final ConsultationRepository consultationRepository;
    private final FactureRepository factureRepository;
    private final MedecinRepository medecinRepository;
    private final DashboardService dashboardService;

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

    @Override
    public ExportResultDTO exportRendezVous(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long medecinId) {
        log.info("Exportation des rendez-vous du {} au {} pour le médecin ID: {}", dateDebut, dateFin, medecinId);

        // Convertir les dates en LocalDateTime pour la requête
        LocalDateTime debutDateTime = dateDebut != null ? dateDebut.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime finDateTime = dateFin != null ? dateFin.atTime(LocalTime.MAX) : LocalDateTime.now().plusMonths(1);

        // Récupérer les rendez-vous
        List<RendezVous> rendezVousList;
        if (medecinId != null) {
            // Remplacer par la méthode disponible dans votre repository
            rendezVousList = rendezVousRepository.findByMedecinIdAndDateHeureBetween(medecinId, debutDateTime, finDateTime);
            // Si cette méthode n'existe pas, vous pouvez utiliser:
            // rendezVousList = rendezVousRepository.findAll().stream()
            //     .filter(rdv -> rdv.getMedecin() != null && rdv.getMedecin().getId().equals(medecinId))
            //     .filter(rdv -> rdv.getDateHeure().isAfter(debutDateTime) && rdv.getDateHeure().isBefore(finDateTime))
            //     .collect(Collectors.toList());
        } else {
            rendezVousList = rendezVousRepository.findByDateHeureBetween(debutDateTime, finDateTime);
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

            // Vérifier si le patient existe et a des propriétés nom et prénom
            if (rdv.getPatient() != null) {
                String patientNom = rdv.getPatient().getNom() != null ? rdv.getPatient().getNom() : "";
                String patientPrenom = rdv.getPatient().getPrenom() != null ? rdv.getPatient().getPrenom() : "";
                row.add(patientNom + " " + patientPrenom);
            } else {
                row.add("");
            }

            // Vérifier si le médecin existe et a des propriétés nom et prénom
            if (rdv.getMedecin() != null) {
                String medecinNom = rdv.getMedecin().getNom() != null ? rdv.getMedecin().getNom() : "";
                String medecinPrenom = rdv.getMedecin().getPrenom() != null ? rdv.getMedecin().getPrenom() : "";
                row.add(medecinNom + " " + medecinPrenom);
            } else {
                row.add("");
            }

            // Vérifier si le statut existe
            row.add(rdv.getStatut() != null ? rdv.getStatut().name() : "");

            // Ajouter motif et notes avec vérification null
            row.add(rdv.getMotif() != null ? rdv.getMotif() : "");
            row.add(rdv.getNotes() != null ? rdv.getNotes() : "");

            data.add(row);
        }

        // Métadonnées pour le document
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des rendez-vous");
        metadata.put("subject", "Rendez-vous du " + dateDebut + " au " + dateFin);
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
        title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return generateExportFile(format, title, "rendez_vous", headers, data, metadata);
    }

    @Override
    public ExportResultDTO exportConsultations(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long medecinId) {
        log.info("Exportation des consultations du {} au {} pour le médecin ID: {}", dateDebut, dateFin, medecinId);

        // Convertir les dates en LocalDateTime pour la requête
        LocalDateTime debutDateTime = dateDebut != null ? dateDebut.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime finDateTime = dateFin != null ? dateFin.atTime(LocalTime.MAX) : LocalDateTime.now().plusMonths(1);

        // Récupérer les consultations
        List<Consultation> consultations;
        if (medecinId != null) {
            // Remplacer par la méthode disponible dans votre repository
            consultations = consultationRepository.findByMedecinIdAndDateBetween(medecinId, debutDateTime, finDateTime);
            // Si cette méthode n'existe pas, vous pouvez utiliser:
            // consultations = consultationRepository.findAll().stream()
            //     .filter(c -> c.getMedecin() != null && c.getMedecin().getId().equals(medecinId))
            //     .filter(c -> c.getDate().isAfter(debutDateTime) && c.getDate().isBefore(finDateTime))
            //     .collect(Collectors.toList());
        } else {
            consultations = consultationRepository.findByDateBetween(debutDateTime, finDateTime);
        }

        // Préparer les en-têtes - adapter selon les propriétés disponibles dans votre entité Consultation
        List<String> headers = Arrays.asList("ID", "Date", "Patient", "Médecin", "Diagnostic", "Notes");

        // Préparer les données
        List<List<String>> data = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Consultation consultation : consultations) {
            List<String> row = new ArrayList<>();
            row.add(consultation.getId().toString());
            row.add(consultation.getDate().format(dateFormatter));

            // Vérifier si le patient existe et a des propriétés nom et prénom
            if (consultation.getDossierMedical().getPatient() != null) {
                String patientNom = consultation.getDossierMedical().getPatient().getNom() != null ? consultation.getDossierMedical().getPatient().getNom() : "";
                String patientPrenom = consultation.getDossierMedical().getPatient().getPrenom() != null ? consultation.getDossierMedical().getPatient().getPrenom() : "";
                row.add(patientNom + " " + patientPrenom);
            } else {
                row.add("");
            }

            // Vérifier si le médecin existe et a des propriétés nom et prénom
            if (consultation.getMedecin() != null) {
                String medecinNom = consultation.getMedecin().getNom() != null ? consultation.getMedecin().getNom() : "";
                String medecinPrenom = consultation.getMedecin().getPrenom() != null ? consultation.getMedecin().getPrenom() : "";
                row.add(medecinNom + " " + medecinPrenom);
            } else {
                row.add("");
            }

            // Ajouter diagnostic et notes avec vérification null
            row.add(consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "");

            // Si votre entité Consultation n'a pas de propriété traitement, supprimez cette ligne
            // row.add(consultation.getTraitement() != null ? consultation.getTraitement() : "");

            row.add(consultation.getNotes() != null ? consultation.getNotes() : "");

            data.add(row);
        }

        // Métadonnées pour le document
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des consultations");
        metadata.put("subject", "Consultations du " + dateDebut + " au " + dateFin);
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
        title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

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
    private ExportResultDTO exportMedicaments(ExportFormat format, Map<String, Object> filtres) {
        log.info("Exportation des médicaments avec filtres: {}", filtres);

        // Cette méthode serait implémentée de manière similaire aux autres méthodes d'exportation
        // en utilisant le repository approprié pour récupérer les données des médicaments

        // Pour l'exemple, nous allons créer des données fictives
        List<String> headers = Arrays.asList("ID", "Nom", "Description", "Dosage", "Forme", "Prix", "Stock");
        List<List<String>> data = new ArrayList<>();

        // Données fictives pour l'exemple
        List<String> row1 = Arrays.asList("1", "Paracétamol", "Analgésique et antipyrétique", "500mg", "Comprimé", "15.50 DH", "250");
        List<String> row2 = Arrays.asList("2", "Amoxicilline", "Antibiotique", "1g", "Gélule", "45.00 DH", "120");
        List<String> row3 = Arrays.asList("3", "Ibuprofène", "Anti-inflammatoire", "400mg", "Comprimé", "22.75 DH", "180");

        data.add(row1);
        data.add(row2);
        data.add(row3);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste des médicaments");

        return generateExportFile(format, "Liste des médicaments", "medicaments", headers, data, metadata);
    }

    // Méthode pour exporter le planning
    private ExportResultDTO exportPlanning(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long departementId) {
        log.info("Exportation du planning du {} au {} pour le département ID: {}", dateDebut, dateFin, departementId);

        // Cette méthode serait implémentée de manière similaire aux autres méthodes d'exportation
        // en utilisant le repository approprié pour récupérer les données du planning

        // Pour l'exemple, nous allons créer des données fictives
        List<String> headers = Arrays.asList("ID", "Date", "Heure début", "Heure fin", "Médecin", "Département", "Statut");
        List<List<String>> data = new ArrayList<>();

        // Données fictives pour l'exemple
        List<String> row1 = Arrays.asList("1", "01/07/2025", "08:00", "12:00", "Dr. Martin", "Cardiologie", "ACTIF");
        List<String> row2 = Arrays.asList("2", "01/07/2025", "14:00", "18:00", "Dr. Dupont", "Cardiologie", "ACTIF");
        List<String> row3 = Arrays.asList("3", "02/07/2025", "09:00", "13:00", "Dr. Martin", "Cardiologie", "ACTIF");

        data.add(row1);
        data.add(row2);
        data.add(row3);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Planning des médecins");

        String title = "Planning";
        if (departementId != null) {
            title += " du département ID: " + departementId;
        }
        title += " du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return generateExportFile(format, title, "planning", headers, data, metadata);
    }

    // Méthode pour exporter le personnel
    private ExportResultDTO exportPersonnel(ExportFormat format, Map<String, Object> filtres) {
        log.info("Exportation du personnel avec filtres: {}", filtres);

        // Cette méthode serait implémentée de manière similaire aux autres méthodes d'exportation
        // en utilisant le repository approprié pour récupérer les données du personnel

        // Pour l'exemple, nous allons créer des données fictives
        List<String> headers = Arrays.asList("ID", "Nom", "Prénom", "Fonction", "Département", "Email", "Téléphone");
        List<List<String>> data = new ArrayList<>();

        // Données fictives pour l'exemple
        List<String> row1 = Arrays.asList("1", "Martin", "Jean", "Médecin", "Cardiologie", "jean.martin@hospital.com", "0612345678");
        List<String> row2 = Arrays.asList("2", "Dupont", "Marie", "Infirmière", "Cardiologie", "marie.dupont@hospital.com", "0623456789");
        List<String> row3 = Arrays.asList("3", "Durand", "Pierre", "Médecin", "Pédiatrie", "pierre.durand@hospital.com", "0634567890");

        data.add(row1);
        data.add(row2);
        data.add(row3);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Liste du personnel");

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