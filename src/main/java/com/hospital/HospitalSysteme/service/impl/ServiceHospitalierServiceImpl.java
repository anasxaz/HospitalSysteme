package com.hospital.HospitalSysteme.service.impl;
import java.util.Objects;
import com.hospital.HospitalSysteme.dto.ServiceCreationDTO;
import com.hospital.HospitalSysteme.dto.ServiceDTO;
import com.hospital.HospitalSysteme.dto.ServiceUpdateDTO;
import com.hospital.HospitalSysteme.entity.ServiceHospitalier;
import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
import com.hospital.HospitalSysteme.mapper.ServiceMapper;
import com.hospital.HospitalSysteme.repository.ServiceRepository;
import com.hospital.HospitalSysteme.service.ServiceHospitalierService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@AllArgsConstructor
@Slf4j
public class ServiceHospitalierServiceImpl implements ServiceHospitalierService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

//    @Override
//    @Transactional
//    public ServiceDTO createService(ServiceCreationDTO serviceCreationDTO) {
//        log.info("Création d'un nouveau service hospitalier : {}", serviceCreationDTO.getNom());
//
//        // Conversion du DTO en entité
//        ServiceHospitalier service = serviceMapper.toEntity(serviceCreationDTO);
//
//        // Si actif n'est pas spécifié, on le met à true par défaut
//        if (service.getActif() == null) {
//            service.setActif(true);
//        }
//
//        // Sauvegarde du service
//        ServiceHospitalier savedService = serviceRepository.save(service);
//
//        log.info("Service hospitalier créé avec succès avec l'ID : {}", savedService.getId());
//
//        // Conversion de l'entité en DTO pour le retour
//        return serviceMapper.toDTO(savedService);
//    }
    @Override
    @Transactional
    public ServiceDTO createService(ServiceCreationDTO serviceCreationDTO) {
        log.info("Création d'un nouveau service hospitalier : {}", serviceCreationDTO.getNom());

        // Conversion du DTO en entité
        ServiceHospitalier service = serviceMapper.toEntity(serviceCreationDTO);

        // Si actif n'est pas spécifié, on le met à true par défaut
        if (service.getActif() == null) {
            service.setActif(true);
        }

        // Gestion automatique du coût si non fourni
        if (service.getCout() == null) {
            // Le coût est calculé automatiquement comme 70% du tarif
            service.setCout(service.getTarif().multiply(new BigDecimal("0.7")));
        }

        // Sauvegarde du service
        ServiceHospitalier savedService = serviceRepository.save(service);

        log.info("Service hospitalier créé avec succès avec l'ID : {}", savedService.getId());

        // Conversion de l'entité en DTO pour le retour
        return serviceMapper.toDTO(savedService);
    }

    @Override
    public ServiceDTO getServiceById(Long serviceId) {
        log.info("Récupération du service hospitalier avec l'ID : {}", serviceId);

        ServiceHospitalier service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service hospitalier non trouvé avec l'ID : " + serviceId));

        return serviceMapper.toDTO(service);
    }

//    @Override
//    @Transactional
//    public ServiceDTO updateService(Long serviceId, ServiceUpdateDTO serviceUpdateDTO) {
//        log.info("Mise à jour du service hospitalier avec l'ID : {}", serviceId);
//
//        // Vérification de l'existence du service
//        ServiceHospitalier service = serviceRepository.findById(serviceId)
//                .orElseThrow(() -> new ResourceNotFoundException("Service hospitalier non trouvé avec l'ID : " + serviceId));
//
//        // Mise à jour des propriétés du service
//        serviceMapper.updateServiceFromDTO(serviceUpdateDTO, service);
//
//        // Sauvegarde des modifications
//        ServiceHospitalier updatedService = serviceRepository.save(service);
//
//        log.info("Service hospitalier mis à jour avec succès");
//
//        return serviceMapper.toDTO(updatedService);
//    }

    @Override
    @Transactional
    public ServiceDTO updateService(Long serviceId, ServiceUpdateDTO serviceUpdateDTO) {
        log.info("Mise à jour du service hospitalier avec l'ID : {}", serviceId);
        log.info("DTO reçu : {}", serviceUpdateDTO);

        // Vérification de l'existence du service
        ServiceHospitalier service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service hospitalier non trouvé avec l'ID : " + serviceId));

        // Mise à jour des propriétés du service via le mapper
        serviceMapper.updateServiceFromDTO(serviceUpdateDTO, service);

        // Si un coût est fourni, l'utiliser. Sinon, calculer automatiquement à partir du tarif
        if (serviceUpdateDTO.getCout() != null) {
            service.setCout(serviceUpdateDTO.getCout());
            log.info("Coût explicite utilisé : {}", serviceUpdateDTO.getCout());
        } else {
            // Recalculer le coût basé sur le tarif actuel
            BigDecimal nouveauCout = service.getTarif().multiply(new BigDecimal("0.7"));
            service.setCout(nouveauCout);
            log.info("Coût calculé automatiquement : {} ({}% de {})", nouveauCout, "70", service.getTarif());
        }

        // Sauvegarde des modifications
        ServiceHospitalier updatedService = serviceRepository.save(service);

        log.info("Service hospitalier mis à jour avec succès");

        return serviceMapper.toDTO(updatedService);
    }

    @Override
    @Transactional
    public void deleteService(Long serviceId) {
        log.info("Suppression du service hospitalier avec l'ID : {}", serviceId);

        // Vérification de l'existence du service
        if (!serviceRepository.existsById(serviceId)) {
            throw new ResourceNotFoundException("Service hospitalier non trouvé avec l'ID : " + serviceId);
        }

        serviceRepository.deleteById(serviceId);

        log.info("Service hospitalier supprimé avec succès");
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        log.info("Récupération de tous les services hospitaliers");

        List<ServiceHospitalier> services = serviceRepository.findAll();

        return serviceMapper.toDTOList(services);
    }

    @Override
    public List<ServiceDTO> getServicesByCategorie(String categorie) {
        log.info("Récupération des services hospitaliers de la catégorie : {}", categorie);

        List<ServiceHospitalier> services = serviceRepository.findByCategorie(categorie);

        return serviceMapper.toDTOList(services);
    }

    @Override
    public List<ServiceDTO> getServicesActifs() {
        log.info("Récupération des services hospitaliers actifs");

        List<ServiceHospitalier> services = serviceRepository.findByActifTrue();

        return serviceMapper.toDTOList(services);
    }

    @Override
    public List<ServiceDTO> getServicesInactifs() {
        log.info("Récupération des services hospitaliers inactifs");

        List<ServiceHospitalier> services = serviceRepository.findByActifFalse();

        return serviceMapper.toDTOList(services);
    }

    @Override
    public List<ServiceDTO> searchServicesByNom(String nom) {
        log.info("Recherche des services hospitaliers contenant : {}", nom);

        List<ServiceHospitalier> services = serviceRepository.findByNomContainingIgnoreCase(nom);

        return serviceMapper.toDTOList(services);
    }

    @Override
    @Transactional
    public ServiceDTO activerService(Long serviceId) {
        log.info("Activation du service hospitalier avec l'ID : {}", serviceId);

        // Récupération du service
        ServiceHospitalier service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service hospitalier non trouvé avec l'ID : " + serviceId));

        // Vérification si le service est déjà actif
        if (service.getActif()) {
            log.info("Le service est déjà actif");
            return serviceMapper.toDTO(service);
        }

        // Activation du service
        service.setActif(true);
        ServiceHospitalier updatedService = serviceRepository.save(service);

        log.info("Service hospitalier activé avec succès");

        return serviceMapper.toDTO(updatedService);
    }

    @Override
    @Transactional
    public ServiceDTO desactiverService(Long serviceId) {
        log.info("Désactivation du service hospitalier avec l'ID : {}", serviceId);

        // Récupération du service
        ServiceHospitalier service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service hospitalier non trouvé avec l'ID : " + serviceId));

        // Vérification si le service est déjà inactif
        if (!service.getActif()) {
            log.info("Le service est déjà inactif");
            return serviceMapper.toDTO(service);
        }

        // Désactivation du service
        service.setActif(false);
        ServiceHospitalier updatedService = serviceRepository.save(service);

        log.info("Service hospitalier désactivé avec succès");

        return serviceMapper.toDTO(updatedService);
    }

    @Override
    @Transactional
    public ServiceDTO updateTarif(Long serviceId, BigDecimal nouveauTarif) {
        log.info("Mise à jour du tarif du service hospitalier avec l'ID : {} à {}", serviceId, nouveauTarif);

        // Validation du nouveau tarif
        if (nouveauTarif == null || nouveauTarif.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le tarif doit être supérieur à zéro");
        }

        // Récupération du service
        ServiceHospitalier service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service hospitalier non trouvé avec l'ID : " + serviceId));

        // Mise à jour du tarif
        service.setTarif(nouveauTarif);
        ServiceHospitalier updatedService = serviceRepository.save(service);

        log.info("Tarif du service hospitalier mis à jour avec succès");

        return serviceMapper.toDTO(updatedService);
    }

    @Override
    public int countServicesByCategorie(String categorie) {
        log.info("Comptage des services hospitaliers de la catégorie : {}", categorie);

        return serviceRepository.countByCategorie(categorie);
    }

    @Override
    public Map<String, Integer> countServicesByAllCategories() {
        log.info("Comptage des services hospitaliers par catégorie");

        List<Object[]> results = serviceRepository.countByAllCategories();

        Map<String, Integer> countByCategory = new HashMap<>();
        for (Object[] result : results) {
            String category = (String) result[0];
            Long count = (Long) result[1];
            countByCategory.put(category != null ? category : "Non catégorisé", count.intValue());
        }

        return countByCategory;
    }

    @Override
    public BigDecimal calculateTotalTarifsByCategorie(String categorie) {
        log.info("Calcul du total des tarifs pour la catégorie : {}", categorie);

        BigDecimal total = serviceRepository.sumTarifsByCategorie(categorie);

        // Si aucun résultat n'est trouvé, retourner zéro
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<ServiceDTO> getTopServicesByTarif(int limit) {
        log.info("Récupération des {} services hospitaliers les plus chers", limit);

        // Utilisation de PageRequest pour limiter le nombre de résultats
        List<ServiceHospitalier> services = serviceRepository.findAllOrderByTarifDesc()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());

        return serviceMapper.toDTOList(services);
    }

    @Override
    public List<String> getAllCategories() {
        log.info("Récupération de toutes les catégories existantes dans la base de données");

        // Récupère uniquement les catégories qui existent réellement dans la table services
        List<String> categories = serviceRepository.findDistinctCategories();

        log.info("Catégories trouvées : {}", categories);

        return categories;
    }
}