package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.export.ExportOptionsDTO;
import com.hospital.HospitalSysteme.dto.export.ExportResultDTO;
import com.hospital.HospitalSysteme.enums.ExportFormat;
import com.hospital.HospitalSysteme.enums.ExportType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExportService {

    /**
     * Exporte les données selon les options spécifiées
     * @param options Options d'exportation (type, format, filtres)
     * @return Résultat de l'exportation contenant le fichier généré
     */
    ExportResultDTO exportData(ExportOptionsDTO options);

    /**
     * Exporte les données des patients
     * @param format Format d'exportation (PDF, EXCEL, CSV)
     * @param filtres Filtres optionnels pour les patients
     * @return Résultat de l'exportation
     */
    ExportResultDTO exportPatients(ExportFormat format, Map<String, Object> filtres);

    /**
     * Exporte les rendez-vous pour une période donnée
     * @param format Format d'exportation
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @param medecinId ID du médecin (optionnel)
     * @return Résultat de l'exportation
     */
    ExportResultDTO exportRendezVous(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long medecinId);

    /**
     * Exporte les consultations pour une période donnée
     * @param format Format d'exportation
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @param medecinId ID du médecin (optionnel)
     * @return Résultat de l'exportation
     */
    ExportResultDTO exportConsultations(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, Long medecinId);

    /**
     * Exporte les factures pour une période donnée
     * @param format Format d'exportation
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @param statut Statut des factures (optionnel)
     * @return Résultat de l'exportation
     */
    ExportResultDTO exportFactures(ExportFormat format, LocalDate dateDebut, LocalDate dateFin, String statut);

    /**
     * Exporte les statistiques du tableau de bord
     * @param format Format d'exportation
     * @param periode Période pour les statistiques (JOUR, SEMAINE, MOIS, ANNEE)
     * @return Résultat de l'exportation
     */
    ExportResultDTO exportStatistiques(ExportFormat format, String periode);
}