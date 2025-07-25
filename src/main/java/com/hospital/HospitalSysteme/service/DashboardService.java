package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.DashboardStatsDTO;
import com.hospital.HospitalSysteme.dto.FacturationSummaryDTO;
import com.hospital.HospitalSysteme.dto.MedecinStatsDTO;
import com.hospital.HospitalSysteme.dto.DepartementStatsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DashboardService {

    /**
     * Récupère les statistiques globales pour le tableau de bord
     * @return DTO contenant toutes les statistiques principales
     */
    DashboardStatsDTO getGlobalStats();

    /**
     * Récupère les statistiques pour une période spécifique
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return DTO contenant les statistiques pour la période spécifiée
     */
    DashboardStatsDTO getStatsByPeriod(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Récupère le résumé de facturation
     * @return DTO contenant les informations de facturation
     */
    FacturationSummaryDTO getFacturationSummary();

    /**
     * Récupère les statistiques par département
     * @return Liste des statistiques par département
     */
    List<DepartementStatsDTO> getStatsByDepartement();

    /**
     * Récupère les statistiques pour un médecin spécifique
     * @param medecinId identifiant du médecin
     * @return DTO contenant les statistiques du médecin
     */
    MedecinStatsDTO getStatsByMedecin(Long medecinId);

    /**
     * Récupère les tendances d'activité sur les derniers mois
     * @param nombreMois nombre de mois à analyser
     * @return Map contenant les tendances par mois
     */
    Map<String, Object> getActivityTrends(int nombreMois);
}