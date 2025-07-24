package com.hospital.HospitalSysteme.controller;

import com.hospital.HospitalSysteme.dto.MedicamentCreationDTO;
import com.hospital.HospitalSysteme.dto.MedicamentDTO;
import com.hospital.HospitalSysteme.dto.MedicamentUpdateDTO;
import com.hospital.HospitalSysteme.service.MedicamentService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicaments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Médicaments", description = "API pour la gestion des médicaments")
public class MedicamentController {

    private final MedicamentService medicamentService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint de test pour vérifier l'accès à l'API des médicaments")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint dans MedicamentController fonctionne");
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau médicament", description = "Permet de créer un nouveau médicament dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médicament créé",
                    content = @Content(schema = @Schema(implementation = MedicamentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIEN')")
    public ResponseEntity<MedicamentDTO> createMedicament(
            @Parameter(description = "Informations du médicament à créer") @Valid @RequestBody MedicamentCreationDTO medicamentCreationDTO) {
        log.info("Demande de création d'un médicament : {}", medicamentCreationDTO.getNom());

        MedicamentDTO createdMedicament = medicamentService.createMedicament(medicamentCreationDTO);
        return new ResponseEntity<>(createdMedicament, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un médicament par ID", description = "Récupère les informations d'un médicament par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médicament trouvé"),
            @ApiResponse(responseCode = "404", description = "Médicament non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<MedicamentDTO> getMedicamentById(
            @Parameter(description = "ID du médicament") @PathVariable Long id) {
        log.info("Demande de récupération du médicament avec l'ID: {}", id);

        MedicamentDTO medicamentDTO = medicamentService.getMedicamentById(id);
        return ResponseEntity.ok(medicamentDTO);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les médicaments", description = "Récupère la liste de tous les médicaments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médicaments récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<MedicamentDTO>> getAllMedicaments() {
        log.info("Demande de récupération de tous les médicaments");

        List<MedicamentDTO> medicamentList = medicamentService.getAllMedicaments();
        return ResponseEntity.ok(medicamentList);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un médicament", description = "Met à jour les informations d'un médicament existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médicament mis à jour"),
            @ApiResponse(responseCode = "404", description = "Médicament non trouvé")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIEN')")
    public ResponseEntity<MedicamentDTO> updateMedicament(
            @Parameter(description = "ID du médicament") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour") @Valid @RequestBody MedicamentUpdateDTO medicamentUpdateDTO) {
        log.info("Demande de mise à jour du médicament avec l'ID: {}", id);

        MedicamentDTO updatedMedicament = medicamentService.updateMedicament(id, medicamentUpdateDTO);
        return ResponseEntity.ok(updatedMedicament);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un médicament", description = "Supprime un médicament du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médicament supprimé"),
            @ApiResponse(responseCode = "404", description = "Médicament non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedicament(
            @Parameter(description = "ID du médicament") @PathVariable Long id) {
        log.info("Demande de suppression du médicament avec l'ID: {}", id);

        medicamentService.deleteMedicament(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des médicaments par nom", description = "Recherche des médicaments par leur nom (recherche partielle)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médicaments correspondants récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<MedicamentDTO>> searchMedicamentsByNom(
            @Parameter(description = "Nom du médicament (recherche partielle)") @RequestParam String nom) {
        log.info("Demande de recherche de médicaments par nom: {}", nom);

        List<MedicamentDTO> medicaments = medicamentService.searchMedicamentsByNom(nom);
        return ResponseEntity.ok(medicaments);
    }

    @GetMapping("/categorie/{categorie}")
    @Operation(summary = "Récupérer les médicaments par catégorie", description = "Récupère tous les médicaments d'une catégorie spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médicaments de la catégorie récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<MedicamentDTO>> getMedicamentsByCategorie(
            @Parameter(description = "Catégorie des médicaments") @PathVariable String categorie) {
        log.info("Demande de récupération des médicaments de la catégorie: {}", categorie);

        List<MedicamentDTO> medicaments = medicamentService.getMedicamentsByCategorie(categorie);
        return ResponseEntity.ok(medicaments);
    }

    @GetMapping("/fabricant/{fabricant}")
    @Operation(summary = "Récupérer les médicaments par fabricant", description = "Récupère tous les médicaments d'un fabricant spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médicaments du fabricant récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<MedicamentDTO>> getMedicamentsByFabricant(
            @Parameter(description = "Fabricant des médicaments") @PathVariable String fabricant) {
        log.info("Demande de récupération des médicaments du fabricant: {}", fabricant);

        List<MedicamentDTO> medicaments = medicamentService.getMedicamentsByFabricant(fabricant);
        return ResponseEntity.ok(medicaments);
    }

    @GetMapping("/ordonnance-requise")
    @Operation(summary = "Récupérer les médicaments par nécessité d'ordonnance",
            description = "Récupère tous les médicaments selon qu'ils nécessitent ou non une ordonnance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médicaments récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<MedicamentDTO>> getMedicamentsByOrdonnanceRequise(
            @Parameter(description = "Ordonnance requise (true/false)") @RequestParam Boolean ordonnanceRequise) {
        log.info("Demande de récupération des médicaments avec ordonnance requise: {}", ordonnanceRequise);

        List<MedicamentDTO> medicaments = medicamentService.getMedicamentsByOrdonnanceRequise(ordonnanceRequise);
        return ResponseEntity.ok(medicaments);
    }

    @GetMapping("/prescription/{prescriptionId}")
    @Operation(summary = "Récupérer les médicaments d'une prescription",
            description = "Récupère tous les médicaments associés à une prescription spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médicaments de la prescription récupérée"),
            @ApiResponse(responseCode = "404", description = "Prescription non trouvée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<MedicamentDTO>> getMedicamentsByPrescription(
            @Parameter(description = "ID de la prescription") @PathVariable Long prescriptionId) {
        log.info("Demande de récupération des médicaments pour la prescription ID: {}", prescriptionId);

        List<MedicamentDTO> medicaments = medicamentService.getMedicamentsByPrescription(prescriptionId);
        return ResponseEntity.ok(medicaments);
    }

    @GetMapping("/categorie/{categorie}/count")
    @Operation(summary = "Compter les médicaments par catégorie",
            description = "Compte le nombre de médicaments dans une catégorie spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de médicaments récupéré")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIEN')")
    public ResponseEntity<Integer> countMedicamentsByCategorie(
            @Parameter(description = "Catégorie des médicaments") @PathVariable String categorie) {
        log.info("Demande de comptage des médicaments de la catégorie: {}", categorie);

        int count = medicamentService.countMedicamentsByCategorie(categorie);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/fabricant/{fabricant}/count")
    @Operation(summary = "Compter les médicaments par fabricant",
            description = "Compte le nombre de médicaments d'un fabricant spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de médicaments récupéré")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIEN')")
    public ResponseEntity<Integer> countMedicamentsByFabricant(
            @Parameter(description = "Fabricant des médicaments") @PathVariable String fabricant) {
        log.info("Demande de comptage des médicaments du fabricant: {}", fabricant);

        int count = medicamentService.countMedicamentsByFabricant(fabricant);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/categories")
    @Operation(summary = "Récupérer toutes les catégories de médicaments",
            description = "Récupère la liste de toutes les catégories distinctes de médicaments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des catégories récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<String>> getAllCategories() {
        log.info("Demande de récupération de toutes les catégories de médicaments");

        List<String> categories = medicamentService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/fabricants")
    @Operation(summary = "Récupérer tous les fabricants de médicaments",
            description = "Récupère la liste de tous les fabricants distincts de médicaments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des fabricants récupérée")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'INFIRMIER', 'PHARMACIEN')")
    public ResponseEntity<List<String>> getAllFabricants() {
        log.info("Demande de récupération de tous les fabricants de médicaments");

        List<String> fabricants = medicamentService.getAllFabricants();
        return ResponseEntity.ok(fabricants);
    }
}