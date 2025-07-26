package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.*;
import com.hospital.HospitalSysteme.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "API pour le tableau de bord hospitalier avec statistiques et analyses en temps réel")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API du dashboard")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans DashboardController fonctionne");
    }

    // ====================== STATISTIQUES GLOBALES ======================

    @GetMapping("/stats/global")
    @Operation(summary = "Récupérer les statistiques globales",
            description = "Récupère un aperçu complet de toutes les statistiques hospitalières en temps réel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques globales récupérées avec succès",
                    content = @Content(schema = @Schema(implementation = DashboardStatsDTO.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<DashboardStatsDTO> getGlobalStats() {
        log.info("Demande de récupération des statistiques globales du dashboard");

        DashboardStatsDTO globalStats = dashboardService.getGlobalStats();
        return ResponseEntity.ok(globalStats);
    }

    @GetMapping("/stats/periode")
    @Operation(summary = "Récupérer les statistiques par période",
            description = "Récupère les statistiques hospitalières pour une période spécifique donnée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques de la période récupérées avec succès",
                    content = @Content(schema = @Schema(implementation = DashboardStatsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dates invalides ou incohérentes")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<DashboardStatsDTO> getStatsByPeriod(
            @Parameter(description = "Date de début de la période (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin de la période (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        log.info("Demande de récupération des statistiques pour la période du {} au {}", dateDebut, dateFin);

        DashboardStatsDTO periodStats = dashboardService.getStatsByPeriod(dateDebut, dateFin);
        return ResponseEntity.ok(periodStats);
    }

    // ====================== STATISTIQUES FINANCIÈRES ======================

    @GetMapping("/stats/facturation")
    @Operation(summary = "Récupérer le résumé de facturation",
            description = "Récupère un résumé complet des données financières et de facturation de l'hôpital")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résumé de facturation récupéré avec succès",
                    content = @Content(schema = @Schema(implementation = FacturationSummaryDTO.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<FacturationSummaryDTO> getFacturationSummary() {
        log.info("Demande de récupération du résumé de facturation");

        FacturationSummaryDTO facturationSummary = dashboardService.getFacturationSummary();
        return ResponseEntity.ok(facturationSummary);
    }

    // ====================== STATISTIQUES PAR DÉPARTEMENT ======================

    @GetMapping("/stats/departements")
    @Operation(summary = "Récupérer les statistiques par département",
            description = "Récupère les statistiques détaillées de performance pour tous les départements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques des départements récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<List<DepartementStatsDTO>> getStatsByDepartement() {
        log.info("Demande de récupération des statistiques par département");

        List<DepartementStatsDTO> departementStats = dashboardService.getStatsByDepartement();
        return ResponseEntity.ok(departementStats);
    }

    // ====================== STATISTIQUES PAR MÉDECIN ======================

    @GetMapping("/stats/medecin/{medecinId}")
    @Operation(summary = "Récupérer les statistiques d'un médecin",
            description = "Récupère les statistiques détaillées de performance pour un médecin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques du médecin récupérées avec succès",
                    content = @Content(schema = @Schema(implementation = MedecinStatsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé"),
            @ApiResponse(responseCode = "400", description = "ID médecin invalide")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<MedecinStatsDTO> getStatsByMedecin(
            @Parameter(description = "ID du médecin") @PathVariable Long medecinId) {
        log.info("Demande de récupération des statistiques pour le médecin ID: {}", medecinId);

        MedecinStatsDTO medecinStats = dashboardService.getStatsByMedecin(medecinId);
        return ResponseEntity.ok(medecinStats);
    }

    // ====================== ANALYSES DE TENDANCES ======================

    @GetMapping("/stats/tendances")
    @Operation(summary = "Récupérer les tendances d'activité",
            description = "Analyse les tendances d'activité hospitalière sur une période donnée en mois")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tendances d'activité récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<Map<String, Object>> getActivityTrends(
            @Parameter(description = "Nombre de mois à analyser (par défaut: 6)")
            @RequestParam(defaultValue = "6") int nombreMois) {
        log.info("Demande d'analyse des tendances d'activité sur {} mois", nombreMois);

        Map<String, Object> activityTrends = dashboardService.getActivityTrends(nombreMois);
        return ResponseEntity.ok(activityTrends);
    }

    // ====================== TABLEAUX DE BORD SPÉCIALISÉS ======================

    @GetMapping("/stats/medical")
    @Operation(summary = "Récupérer les statistiques médicales",
            description = "Récupère un tableau de bord spécialisé pour les activités médicales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques médicales récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<Map<String, Object>> getMedicalDashboard() {
        log.info("Demande de récupération du tableau de bord médical");

        // Combiner plusieurs statistiques pour un dashboard médical complet
        DashboardStatsDTO globalStats = dashboardService.getGlobalStats();
        List<DepartementStatsDTO> departementStats = dashboardService.getStatsByDepartement();

        Map<String, Object> medicalDashboard = Map.of(
                "statistiquesGlobales", globalStats,
                "statistiquesDepartements", departementStats,
                "tendancesActivite", dashboardService.getActivityTrends(3)
        );

        return ResponseEntity.ok(medicalDashboard);
    }

    @GetMapping("/stats/administratif")
    @Operation(summary = "Récupérer les statistiques administratives",
            description = "Récupère un tableau de bord spécialisé pour la gestion administrative")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques administratives récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Map<String, Object>> getAdministrativeDashboard() {
        log.info("Demande de récupération du tableau de bord administratif");

        // Combiner statistiques globales et financières pour un dashboard administratif
        DashboardStatsDTO globalStats = dashboardService.getGlobalStats();
        FacturationSummaryDTO facturationSummary = dashboardService.getFacturationSummary();
        Map<String, Object> activityTrends = dashboardService.getActivityTrends(12);

        Map<String, Object> adminDashboard = Map.of(
                "statistiquesGlobales", globalStats,
                "resumeFacturation", facturationSummary,
                "tendancesAnnuelles", activityTrends
        );

        return ResponseEntity.ok(adminDashboard);
    }

    @GetMapping("/stats/financier")
    @Operation(summary = "Récupérer les statistiques financières détaillées",
            description = "Récupère un tableau de bord spécialisé pour l'analyse financière approfondie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques financières détaillées récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF')")
    public ResponseEntity<Map<String, Object>> getFinancialDashboard() {
        log.info("Demande de récupération du tableau de bord financier");

        FacturationSummaryDTO facturationSummary = dashboardService.getFacturationSummary();
        Map<String, Object> financialTrends = dashboardService.getActivityTrends(12);

        Map<String, Object> financialDashboard = Map.of(
                "resumeFacturation", facturationSummary,
                "tendancesRevenus", financialTrends,
                "periodeAnalyse", "12 derniers mois"
        );

        return ResponseEntity.ok(financialDashboard);
    }

    // ====================== STATISTIQUES EN TEMPS RÉEL ======================

    @GetMapping("/stats/temps-reel")
    @Operation(summary = "Récupérer les statistiques en temps réel",
            description = "Récupère les indicateurs clés de performance en temps réel pour monitoring")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques temps réel récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<Map<String, Object>> getRealTimeStats() {
        log.info("Demande de récupération des statistiques en temps réel");

        DashboardStatsDTO todayStats = dashboardService.getStatsByPeriod(LocalDate.now(), LocalDate.now());

        Map<String, Object> realTimeStats = Map.of(
                "rendezVousAujourdhui", todayStats.getRendezVousAujourdhui() != null ? todayStats.getRendezVousAujourdhui() : 0L,
                "consultationsAujourdhui", todayStats.getConsultationsAujourdhui() != null ? todayStats.getConsultationsAujourdhui() : 0L,
                "revenusJournaliers", todayStats.getRevenusJournaliers() != null ? todayStats.getRevenusJournaliers() : 0,
                "timestamp", java.time.LocalDateTime.now(),
                "statut", "ACTIF"
        );

        return ResponseEntity.ok(realTimeStats);
    }

    // ====================== STATISTIQUES COMPARATIVES ======================

    @GetMapping("/stats/comparaison")
    @Operation(summary = "Récupérer les statistiques comparatives",
            description = "Compare les performances entre différentes périodes pour analyse de tendances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques comparatives récupérées avec succès")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CADRE_ADMINISTRATIF', 'MEDECIN')")
    public ResponseEntity<Map<String, Object>> getComparativeStats(
            @Parameter(description = "Première date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut1,
            @Parameter(description = "Première date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin1,
            @Parameter(description = "Deuxième date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut2,
            @Parameter(description = "Deuxième date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin2) {
        log.info("Demande de comparaison entre les périodes [{} - {}] et [{} - {}]",
                dateDebut1, dateFin1, dateDebut2, dateFin2);

        DashboardStatsDTO periode1 = dashboardService.getStatsByPeriod(dateDebut1, dateFin1);
        DashboardStatsDTO periode2 = dashboardService.getStatsByPeriod(dateDebut2, dateFin2);

        Map<String, Object> comparison = Map.of(
                "periode1", Map.of(
                        "dateDebut", dateDebut1,
                        "dateFin", dateFin1,
                        "statistiques", periode1
                ),
                "periode2", Map.of(
                        "dateDebut", dateDebut2,
                        "dateFin", dateFin2,
                        "statistiques", periode2
                ),
                "evolutionRendezVous", calculateEvolution(periode1.getTotalRendezVous(), periode2.getTotalRendezVous()),
                "evolutionConsultations", calculateEvolution(periode1.getConsultationsAujourdhui(), periode2.getConsultationsAujourdhui()),
                "evolutionRevenus", calculateEvolution(periode1.getRevenusJournaliers(), periode2.getRevenusJournaliers())
        );

        return ResponseEntity.ok(comparison);
    }

    // ====================== MÉTHODES UTILITAIRES ======================

    private Double calculateEvolution(Long valeur1, Long valeur2) {
        if (valeur1 == null || valeur2 == null || valeur1 == 0) {
            return 0.0;
        }
        return ((double) (valeur2 - valeur1) / valeur1) * 100;
    }

    private Double calculateEvolution(java.math.BigDecimal valeur1, java.math.BigDecimal valeur2) {
        if (valeur1 == null || valeur2 == null || valeur1.compareTo(java.math.BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return valeur2.subtract(valeur1).divide(valeur1, 4, java.math.RoundingMode.HALF_UP)
                .multiply(java.math.BigDecimal.valueOf(100)).doubleValue();
    }
}