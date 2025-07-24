//package com.hospital.HospitalSysteme.controller;
//
//import com.hospital.HospitalSysteme.dto.PatientCreationDTO;
//import com.hospital.HospitalSysteme.dto.PatientDTO;
//import com.hospital.HospitalSysteme.service.PatientService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/patient-registration")
//@RequiredArgsConstructor
//@Slf4j
//@Tag(name = "Inscription Patient", description = "API pour l'inscription des patients")
//public class PatientRegistrationController {
//
//    private final PatientService patientService;
//
//    @PostMapping
//    @Operation(summary = "Inscrire un nouveau patient", description = "Permet à un patient de créer son compte")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Patient créé",
//                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
//            @ApiResponse(responseCode = "400", description = "Données invalides ou email déjà utilisé")
//    })
//    public ResponseEntity<PatientDTO> registerPatient(
//            @Parameter(description = "Informations d'inscription du patient") @Valid @RequestBody PatientCreationDTO patientCreationDTO) {
//        log.info("Demande d'inscription d'un nouveau patient avec l'email : {}", patientCreationDTO.getEmail());
//        PatientDTO createdPatient = patientService.createPatient(patientCreationDTO);
//        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
//    }
//}