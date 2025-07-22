package com.hospital.HospitalSysteme.service;

import com.hospital.HospitalSysteme.dto.ServiceCreationDTO;
import com.hospital.HospitalSysteme.dto.ServiceDTO;
import com.hospital.HospitalSysteme.dto.ServiceUpdateDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ServiceHospitalierService {

    // Opérations CRUD de base
    ServiceDTO createService(ServiceCreationDTO serviceCreationDTO);
    ServiceDTO getServiceById(Long serviceId);
    ServiceDTO updateService(Long serviceId, ServiceUpdateDTO serviceUpdateDTO);
    void deleteService(Long serviceId);

    // Opérations de recherche et filtrage
    List<ServiceDTO> getAllServices();
    List<ServiceDTO> getServicesByCategorie(String categorie);
    List<ServiceDTO> getServicesActifs();
    List<ServiceDTO> getServicesInactifs();
    List<ServiceDTO> searchServicesByNom(String nom);

    // Opérations de gestion
    ServiceDTO activerService(Long serviceId);
    ServiceDTO desactiverService(Long serviceId);
    ServiceDTO updateTarif(Long serviceId, BigDecimal nouveauTarif);

    // Statistiques et rapports
    int countServicesByCategorie(String categorie);
    Map<String, Integer> countServicesByAllCategories();
    BigDecimal calculateTotalTarifsByCategorie(String categorie);
    List<ServiceDTO> getTopServicesByTarif(int limit);
}