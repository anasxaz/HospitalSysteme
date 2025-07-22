package com.hospital.HospitalSysteme.service.impl;

import com.hospital.HospitalSysteme.dto.DashboardStatsDTO;
import com.hospital.HospitalSysteme.dto.DepartementStatsDTO;
import com.hospital.HospitalSysteme.dto.FacturationSummaryDTO;
import com.hospital.HospitalSysteme.dto.MedecinStatsDTO;
import com.hospital.HospitalSysteme.repository.*;
import com.hospital.HospitalSysteme.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final InfirmierRepository infirmierRepository;
    private final RendezVousRepository rendezVousRepository;
    private final ConsultationRepository consultationRepository;
    private final FactureRepository factureRepository;
    private final DepartementRepository departementRepository;

    @Override
    public DashboardStatsDTO getGlobalStats() {
        // Implémentez la logique pour récupérer les statistiques globales
        // Utilisez les méthodes des repositories pour obtenir les données

        // Exemple de structure:
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // Comptages de base
        stats.setTotalPatients(patientRepository.count());
        stats.setTotalMedecins(medecinRepository.count());
        stats.setTotalInfirmiers(infirmierRepository.count());
        stats.setTotalRendezVous(rendezVousRepository.count());

        // Statistiques du jour
        LocalDateTime debutJour = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime finJour = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        stats.setRendezVousAujourdhui(rendezVousRepository.countByDateHeureGreaterThanEqualAndDateHeureLessThan(debutJour, finJour));
        stats.setConsultationsAujourdhui(consultationRepository.countByDateGreaterThanEqualAndDateLessThan(debutJour, finJour));

        // Revenus
        stats.setRevenusJournaliers(factureRepository.sumMontantTotalByDateBetween(debutJour, finJour));

        // Statistiques hebdomadaires et mensuelles (à implémenter)

        // Mappages
        stats.setRendezVousParStatut(getRendezVousParStatut());
        stats.setPatientsParGroupeSanguin(getPatientsParGroupeSanguin());
        stats.setConsultationsParMois(getConsultationsParMois());

        return stats;
    }

    @Override
    public DashboardStatsDTO getStatsByPeriod(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null || dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("Les dates de début et de fin sont invalides");
        }

        DashboardStatsDTO stats = new DashboardStatsDTO();

        LocalDateTime debutDateTime = dateDebut.atStartOfDay();
        LocalDateTime finDateTime = dateFin.atTime(LocalTime.MAX);

        // Comptages pour la période spécifiée
        stats.setTotalRendezVous(rendezVousRepository.countByDateHeureBetween(debutDateTime, finDateTime));
        stats.setConsultationsAujourdhui(consultationRepository.countByDateBetween(debutDateTime, finDateTime));

        // Revenus pour la période
        BigDecimal revenus = factureRepository.sumMontantTotalByDateBetween(debutDateTime, finDateTime);
        stats.setRevenusJournaliers(revenus); // On utilise ce champ pour stocker les revenus de la période

        // On peut laisser les autres champs à null ou à 0 car ils ne sont pas pertinents pour une période spécifique
        // Ou on peut les calculer si nécessaire

        return stats;
    }

    @Override
    public FacturationSummaryDTO getFacturationSummary() {
        FacturationSummaryDTO summary = new FacturationSummaryDTO();

        // Calcul des montants
        LocalDateTime debutAnnee = LocalDate.of(Year.now().getValue(), 1, 1).atStartOfDay();
        LocalDateTime finAnnee = LocalDate.of(Year.now().getValue(), 12, 31).atTime(LocalTime.MAX);

        BigDecimal montantTotal = factureRepository.sumMontantTotalByDateBetween(debutAnnee, finAnnee);
        BigDecimal montantPaye = factureRepository.sumMontantPayeByDateBetween(debutAnnee, finAnnee);

        summary.setMontantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO);
        summary.setMontantPaye(montantPaye != null ? montantPaye : BigDecimal.ZERO);
        summary.setMontantDu(summary.getMontantTotal().subtract(summary.getMontantPaye()));

        // Comptage des factures par statut
        List<Object[]> statutData = factureRepository.countFacturesByStatut();
        Map<String, Long> statutCounts = new HashMap<>();

        for (Object[] row : statutData) {
            String statut = row[0].toString();
            Long count = (Long) row[1];

            if ("PAYE".equals(statut)) {
                summary.setFacturesPayees(count);
            } else if ("EN_ATTENTE".equals(statut)) {
                summary.setFacturesEnAttente(count);
            } else if ("ANNULE".equals(statut)) {
                summary.setFacturesAnnulees(count);
            }
        }

        // Revenus par service
        List<Object[]> serviceData = factureRepository.sumMontantByService();
        Map<String, BigDecimal> revenusParService = new HashMap<>();

        for (Object[] row : serviceData) {
            String service = (String) row[0];
            BigDecimal montant = (BigDecimal) row[1];
            revenusParService.put(service, montant);
        }
        summary.setRevenusParService(revenusParService);

        // Revenus par mois
        List<Object[]> moisData = factureRepository.sumMontantByMois(Year.now().getValue());
        Map<String, BigDecimal> revenusParMois = new HashMap<>();

        String[] nomsMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

        for (Object[] row : moisData) {
            Integer mois = (Integer) row[0];
            BigDecimal montant = (BigDecimal) row[1];
            revenusParMois.put(nomsMois[mois - 1], montant);
        }
        summary.setRevenusParMois(revenusParMois);

        return summary;
    }

    @Override
    public List<DepartementStatsDTO> getStatsByDepartement() {
        // Récupérer les données de base des départements
        List<Object[]> medecinsByDept = departementRepository.countMedecinsByDepartement();
        List<Object[]> infirmiersByDept = departementRepository.countInfirmiersByDepartement();

        Map<Long, DepartementStatsDTO> statsMap = new HashMap<>();

        // Traiter les données des médecins par département
        for (Object[] row : medecinsByDept) {
            Long deptId = (Long) row[0];
            String deptNom = (String) row[1];
            Long nombreMedecins = (Long) row[2];

            DepartementStatsDTO stats = new DepartementStatsDTO();
            stats.setDepartementId(deptId);
            stats.setNom(deptNom);
            stats.setNombreMedecins(nombreMedecins);
            stats.setNombreInfirmiers(0L); // Valeur par défaut
            stats.setNombreConsultations(0L);
            stats.setNombreRendezVous(0L);
            stats.setRevenusGeneres(BigDecimal.ZERO);
            stats.setTauxOccupation(0.0);
            stats.setNombrePatientsTraites(0L);

            statsMap.put(deptId, stats);
        }

        // Traiter les données des infirmiers par département
        for (Object[] row : infirmiersByDept) {
            Long deptId = (Long) row[0];
            Long nombreInfirmiers = (Long) row[2];

            if (statsMap.containsKey(deptId)) {
                statsMap.get(deptId).setNombreInfirmiers(nombreInfirmiers);
            }
        }

        // Convertir la map en liste
        return statsMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public MedecinStatsDTO getStatsByMedecin(Long medecinId) {
        if (medecinId == null) {
            throw new IllegalArgumentException("L'ID du médecin ne peut pas être null");
        }

        // Vérifier si le médecin existe
        var medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + medecinId));

        MedecinStatsDTO stats = new MedecinStatsDTO();
        stats.setMedecinId(medecinId);
        stats.setNom(medecin.getNom());
        stats.setPrenom(medecin.getPrenom());
        stats.setSpecialite(medecin.getSpecialite());

        // Nombre total de consultations
        stats.setTotalConsultations((long) consultationRepository.countByMedecinId(medecinId));

        // Consultations par jour de la semaine
        List<Object[]> joursData = rendezVousRepository.countRendezVousByJourSemaineForMedecin(medecinId);
        Map<String, Long> consultationsParJour = new HashMap<>();

        String[] joursNoms = {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};

        // Initialiser tous les jours à 0
        for (String jour : joursNoms) {
            consultationsParJour.put(jour, 0L);
        }

        // Remplir avec les données réelles
        for (Object[] row : joursData) {
            Integer jourIndex = (Integer) row[0];
            Long count = (Long) row[1];
            consultationsParJour.put(joursNoms[jourIndex - 1], count);
        }

        stats.setConsultationsParJourSemaine(consultationsParJour);

        // Autres statistiques à implémenter selon les besoins
        // ...

        return stats;
    }

    @Override
    public Map<String, Object> getActivityTrends(int nombreMois) {
        if (nombreMois <= 0) {
            nombreMois = 6; // Valeur par défaut
        }

        Map<String, Object> trends = new HashMap<>();

        // Calculer la période
        LocalDate finPeriode = LocalDate.now();
        LocalDate debutPeriode = finPeriode.minusMonths(nombreMois);

        // Tendances des rendez-vous
        Map<String, Long> rendezVousTrend = new HashMap<>();
        Map<String, Long> consultationsTrend = new HashMap<>();
        Map<String, BigDecimal> revenusTrend = new HashMap<>();

        for (int i = 0; i < nombreMois; i++) {
            LocalDate moisCourant = debutPeriode.plusMonths(i);
            String nomMois = moisCourant.getMonth().toString() + " " + moisCourant.getYear();

            LocalDateTime debutMois = moisCourant.withDayOfMonth(1).atStartOfDay();
            LocalDateTime finMois = moisCourant.withDayOfMonth(moisCourant.lengthOfMonth()).atTime(LocalTime.MAX);

            // Nombre de rendez-vous pour ce mois
            Long nbRendezVous = rendezVousRepository.countByDateHeureBetween(debutMois, finMois);
            rendezVousTrend.put(nomMois, nbRendezVous);

            // Nombre de consultations pour ce mois
            Long nbConsultations = consultationRepository.countByDateBetween(debutMois, finMois);
            consultationsTrend.put(nomMois, nbConsultations);

            // Revenus pour ce mois
            BigDecimal revenus = factureRepository.sumMontantTotalByDateBetween(debutMois, finMois);
            revenusTrend.put(nomMois, revenus != null ? revenus : BigDecimal.ZERO);
        }

        trends.put("rendezVous", rendezVousTrend);
        trends.put("consultations", consultationsTrend);
        trends.put("revenus", revenusTrend);

        return trends;
    }

    // Implémentez les autres méthodes de l'interface...

    // Méthodes utilitaires pour construire les mappages
    private Map<String, Long> getRendezVousParStatut() {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> data = rendezVousRepository.countRendezVousByStatut();

        for (Object[] row : data) {
            String statut = row[0].toString();
            Long count = (Long) row[1];
            result.put(statut, count);
        }

        return result;
    }

    private Map<String, Long> getPatientsParGroupeSanguin() {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> data = patientRepository.countPatientsByGroupeSanguin();

        for (Object[] row : data) {
            String groupeSanguin = row[0] != null ? row[0].toString() : "NON_SPECIFIE";
            Long count = (Long) row[1];
            result.put(groupeSanguin, count);
        }

        return result;
    }

    private Map<String, Long> getConsultationsParMois() {
        Map<String, Long> result = new HashMap<>();
        int anneeActuelle = Year.now().getValue();
        List<Object[]> data = consultationRepository.countConsultationsByMois(anneeActuelle);

        // Noms des mois en français
        String[] nomsMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

        // Initialiser tous les mois à 0
        for (int i = 0; i < 12; i++) {
            result.put(nomsMois[i], 0L);
        }

        // Remplir avec les données réelles
        for (Object[] row : data) {
            Integer mois = (Integer) row[0];
            Long count = (Long) row[1];
            // Les mois dans SQL commencent généralement à 1, donc on soustrait 1 pour l'index du tableau
            result.put(nomsMois[mois - 1], count);
        }

        return result;
    }
}