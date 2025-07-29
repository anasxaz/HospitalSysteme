package com.hospital.HospitalSysteme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.HospitalSysteme.dto.ServiceCreationDTO;
import com.hospital.HospitalSysteme.dto.ServiceDTO;
import com.hospital.HospitalSysteme.dto.ServiceUpdateDTO;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.service.ServiceHospitalierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceHospitalierController.class)
@DisplayName("Tests du contrôleur ServiceHospitalier")
class ServiceHospitalierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceHospitalierService serviceHospitalierService;

    @Autowired
    private ObjectMapper objectMapper;

    private ServiceDTO serviceDTO;
    private ServiceCreationDTO serviceCreationDTO;
    private ServiceUpdateDTO serviceUpdateDTO;

    @BeforeEach
    void setUp() {
        // Initialisation des objets de test
        serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setNom("Radiologie");
        serviceDTO.setDescription("Service de radiologie médicale");
        serviceDTO.setTarif(new BigDecimal("150.00"));
        serviceDTO.setCategorie("Imagerie");
        serviceDTO.setActif(true);

        serviceCreationDTO = new ServiceCreationDTO();
        serviceCreationDTO.setNom("Radiologie");
        serviceCreationDTO.setDescription("Service de radiologie médicale");
        serviceCreationDTO.setTarif(new BigDecimal("150.00"));
        serviceCreationDTO.setCategorie("Imagerie");
        serviceCreationDTO.setActif(true);

        serviceUpdateDTO = new ServiceUpdateDTO();
        serviceUpdateDTO.setNom("Radiologie Avancée");
        serviceUpdateDTO.setDescription("Service de radiologie médicale avancée");
        serviceUpdateDTO.setTarif(new BigDecimal("200.00"));
        serviceUpdateDTO.setCategorie("Imagerie");
        serviceUpdateDTO.setActif(true);
    }

    @Nested
    @DisplayName("Tests de gestion CRUD")
    class CrudOperationsTests {

        @Test
        @DisplayName("POST /api/services-hospitaliers - Création réussie d'un service")
        @WithMockUser(roles = "ADMIN")
        void createService_Success() throws Exception {
            // Given
            when(serviceHospitalierService.createService(any(ServiceCreationDTO.class)))
                    .thenReturn(serviceDTO);

            // When & Then
            mockMvc.perform(post("/api/services-hospitaliers")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(serviceCreationDTO)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.nom", is("Radiologie")))
                    .andExpected(jsonPath("$.tarif", is(150.00)))
                    .andExpect(jsonPath("$.categorie", is("Imagerie")))
                    .andExpect(jsonPath("$.actif", is(true)));

            verify(serviceHospitalierService, times(1)).createService(any(ServiceCreationDTO.class));
        }

        @Test
        @DisplayName("POST /api/services-hospitaliers - Échec de validation")
        @WithMockUser(roles = "ADMIN")
        void createService_ValidationFailure() throws Exception {
            // Given - ServiceCreationDTO avec des données invalides
            ServiceCreationDTO invalidDTO = new ServiceCreationDTO();
            invalidDTO.setNom(""); // Nom vide
            invalidDTO.setTarif(new BigDecimal("-10")); // Tarif négatif

            // When & Then
            mockMvc.perform(post("/api/services-hospitaliers")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(serviceHospitalierService, never()).createService(any(ServiceCreationDTO.class));
        }

        @Test
        @DisplayName("POST /api/services-hospitaliers - Accès refusé pour rôle non autorisé")
        @WithMockUser(roles = "INFIRMIER")
        void createService_AccessDenied() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/services-hospitaliers")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(serviceCreationDTO)))
                    .andDo(print())
                    .andExpect(status().isForbidden());

            verify(serviceHospitalierService, never()).createService(any(ServiceCreationDTO.class));
        }

        @Test
        @DisplayName("GET /api/services-hospitaliers/{id} - Récupération réussie")
        @WithMockUser(roles = "ADMIN")
        void getServiceById_Success() throws Exception {
            // Given
            when(serviceHospitalierService.getServiceById(1L)).thenReturn(serviceDTO);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.nom", is("Radiologie")))
                    .andExpect(jsonPath("$.tarif", is(150.00)));

            verify(serviceHospitalierService, times(1)).getServiceById(1L);
        }

        @Test
        @DisplayName("GET /api/services-hospitaliers/{id} - Service non trouvé")
        @WithMockUser(roles = "ADMIN")
        void getServiceById_NotFound() throws Exception {
            // Given
            when(serviceHospitalierService.getServiceById(99L))
                    .thenThrow(new ResourceNotFoundException("Service hospitalier non trouvé avec l'ID : 99"));

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/99"))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(serviceHospitalierService, times(1)).getServiceById(99L);
        }

        @Test
        @DisplayName("GET /api/services-hospitaliers - Récupération de tous les services")
        @WithMockUser(roles = "ADMIN")
        void getAllServices_Success() throws Exception {
            // Given
            List<ServiceDTO> servicesList = Arrays.asList(serviceDTO, serviceDTO);
            when(serviceHospitalierService.getAllServices()).thenReturn(servicesList);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].nom", is("Radiologie")))
                    .andExpect(jsonPath("$[1].nom", is("Radiologie")));

            verify(serviceHospitalierService, times(1)).getAllServices();
        }

        @Test
        @DisplayName("PUT /api/services-hospitaliers/{id} - Mise à jour réussie")
        @WithMockUser(roles = "ADMIN")
        void updateService_Success() throws Exception {
            // Given
            ServiceDTO updatedServiceDTO = new ServiceDTO();
            updatedServiceDTO.setId(1L);
            updatedServiceDTO.setNom("Radiologie Avancée");
            updatedServiceDTO.setTarif(new BigDecimal("200.00"));

            when(serviceHospitalierService.updateService(eq(1L), any(ServiceUpdateDTO.class)))
                    .thenReturn(updatedServiceDTO);

            // When & Then
            mockMvc.perform(put("/api/services-hospitaliers/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(serviceUpdateDTO)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.nom", is("Radiologie Avancée")))
                    .andExpect(jsonPath("$.tarif", is(200.00)));

            verify(serviceHospitalierService, times(1)).updateService(eq(1L), any(ServiceUpdateDTO.class));
        }

        @Test
        @DisplayName("DELETE /api/services-hospitaliers/{id} - Suppression réussie")
        @WithMockUser(roles = "ADMIN")
        void deleteService_Success() throws Exception {
            // Given
            doNothing().when(serviceHospitalierService).deleteService(1L);

            // When & Then
            mockMvc.perform(delete("/api/services-hospitaliers/1")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(serviceHospitalierService, times(1)).deleteService(1L);
        }

        @Test
        @DisplayName("DELETE /api/services-hospitaliers/{id} - Accès refusé pour CADRE_ADMINISTRATIF")
        @WithMockUser(roles = "CADRE_ADMINISTRATIF")
        void deleteService_AccessDenied() throws Exception {
            // When & Then
            mockMvc.perform(delete("/api/services-hospitaliers/1")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isForbidden());

            verify(serviceHospitalierService, never()).deleteService(anyLong());
        }
    }

    @Nested
    @DisplayName("Tests de filtres et recherches")
    class FilterAndSearchTests {

        @Test
        @DisplayName("GET /api/services-hospitaliers/categorie/{categorie} - Récupération par catégorie")
        @WithMockUser(roles = "ADMIN")
        void getServicesByCategorie_Success() throws Exception {
            // Given
            List<ServiceDTO> services = Arrays.asList(serviceDTO);
            when(serviceHospitalierService.getServicesByCategorie("Imagerie")).thenReturn(services);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/categorie/Imagerie"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].categorie", is("Imagerie")));

            verify(serviceHospitalierService, times(1)).getServicesByCategorie("Imagerie");
        }

        @Test
        @DisplayName("GET /api/services-hospitaliers/actifs - Récupération des services actifs")
        @WithMockUser(roles = "MEDECIN")
        void getServicesActifs_Success() throws Exception {
            // Given
            List<ServiceDTO> services = Arrays.asList(serviceDTO);
            when(serviceHospitalierService.getServicesActifs()).thenReturn(services);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/actifs"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].actif", is(true)));

            verify(serviceHospitalierService, times(1)).getServicesActifs();
        }

        @Test
        @DisplayName("GET /api/services-hospitaliers/search - Recherche par nom")
        @WithMockUser(roles = "INFIRMIER")
        void searchServicesByNom_Success() throws Exception {
            // Given
            List<ServiceDTO> services = Arrays.asList(serviceDTO);
            when(serviceHospitalierService.searchServicesByNom("Radio")).thenReturn(services);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/search")
                            .param("nom", "Radio"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nom", containsString("Radio")));

            verify(serviceHospitalierService, times(1)).searchServicesByNom("Radio");
        }
    }

    @Nested
    @DisplayName("Tests d'activation/désactivation")
    class ActivationTests {

        @Test
        @DisplayName("PUT /api/services-hospitaliers/{id}/activer - Activation réussie")
        @WithMockUser(roles = "ADMIN")
        void activerService_Success() throws Exception {
            // Given
            when(serviceHospitalierService.activerService(1L)).thenReturn(serviceDTO);

            // When & Then
            mockMvc.perform(put("/api/services-hospitaliers/1/activer")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.actif", is(true)));

            verify(serviceHospitalierService, times(1)).activerService(1L);
        }

        @Test
        @DisplayName("PUT /api/services-hospitaliers/{id}/desactiver - Désactivation réussie")
        @WithMockUser(roles = "CADRE_ADMINISTRATIF")
        void desactiverService_Success() throws Exception {
            // Given
            ServiceDTO inactiveService = new ServiceDTO();
            inactiveService.setId(1L);
            inactiveService.setActif(false);
            when(serviceHospitalierService.desactiverService(1L)).thenReturn(inactiveService);

            // When & Then
            mockMvc.perform(put("/api/services-hospitaliers/1/desactiver")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.actif", is(false)));

            verify(serviceHospitalierService, times(1)).desactiverService(1L);
        }
    }

    @Nested
    @DisplayName("Tests de gestion des tarifs")
    class TarifTests {

        @Test
        @DisplayName("PUT /api/services-hospitaliers/{id}/tarif - Mise à jour de tarif réussie")
        @WithMockUser(roles = "ADMIN")
        void updateTarif_Success() throws Exception {
            // Given
            BigDecimal nouveauTarif = new BigDecimal("250.00");
            ServiceDTO updatedService = new ServiceDTO();
            updatedService.setId(1L);
            updatedService.setTarif(nouveauTarif);
            when(serviceHospitalierService.updateTarif(1L, nouveauTarif)).thenReturn(updatedService);

            // When & Then
            mockMvc.perform(put("/api/services-hospitaliers/1/tarif")
                            .with(csrf())
                            .param("nouveauTarif", "250.00"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.tarif", is(250.00)));

            verify(serviceHospitalierService, times(1)).updateTarif(1L, nouveauTarif);
        }

        @Test
        @DisplayName("GET /api/services-hospitaliers/top-tarifs - Services les plus chers")
        @WithMockUser(roles = "ADMIN")
        void getTopServicesByTarif_Success() throws Exception {
            // Given
            List<ServiceDTO> topServices = Arrays.asList(serviceDTO);
            when(serviceHospitalierService.getTopServicesByTarif(5)).thenReturn(topServices);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/top-tarifs")
                            .param("limit", "5"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(serviceHospitalierService, times(1)).getTopServicesByTarif(5);
        }
    }

    @Nested
    @DisplayName("Tests de statistiques")
    class StatisticsTests {

        @Test
        @DisplayName("GET /api/services-hospitaliers/stats/count-by-categorie/{categorie} - Comptage par catégorie")
        @WithMockUser(roles = "ADMIN")
        void countServicesByCategorie_Success() throws Exception {
            // Given
            when(serviceHospitalierService.countServicesByCategorie("Imagerie")).thenReturn(5);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/stats/count-by-categorie/Imagerie"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string("5"));

            verify(serviceHospitalierService, times(1)).countServicesByCategorie("Imagerie");
        }

        @Test
        @DisplayName("GET /api/services-hospitaliers/stats/count-by-all-categories - Statistiques globales")
        @WithMockUser(roles = "CADRE_ADMINISTRATIF")
        void countServicesByAllCategories_Success() throws Exception {
            // Given
            Map<String, Integer> stats = new HashMap<>();
            stats.put("Imagerie", 5);
            stats.put("Chirurgie", 3);
            when(serviceHospitalierService.countServicesByAllCategories()).thenReturn(stats);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/stats/count-by-all-categories"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.Imagerie", is(5)))
                    .andExpect(jsonPath("$.Chirurgie", is(3)));

            verify(serviceHospitalierService, times(1)).countServicesByAllCategories();
        }

        @Test
        @DisplayName("GET /api/services-hospitaliers/stats/total-tarifs-by-categorie/{categorie} - Total des tarifs")
        @WithMockUser(roles = "ADMIN")
        void calculateTotalTarifsByCategorie_Success() throws Exception {
            // Given
            BigDecimal total = new BigDecimal("1500.00");
            when(serviceHospitalierService.calculateTotalTarifsByCategorie("Imagerie")).thenReturn(total);

            // When & Then
            mockMvc.perform(get("/api/services-hospitaliers/stats/total-tarifs-by-categorie/Imagerie"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string("1500.00"));

            verify(serviceHospitalierService, times(1)).calculateTotalTarifsByCategorie("Imagerie");
        }
    }

    @Test
    @DisplayName("GET /api/services-hospitaliers/test - Test endpoint")
    void testEndpoint_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/services-hospitaliers/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Test endpoint dans ServiceHospitalierController fonctionne"));
    }
}