package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.*;
import org.pentagone.business.zentracore.hr.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationDto> createApplication(
            @RequestPart("application") ApplicationDto applicationDto,
            @RequestPart(value = "cv") MultipartFile cv,
            @RequestPart(value = "motivationLetter") MultipartFile motivationLetter
    ) {
        ApplicationDto created = applicationService.createApplication(applicationDto, cv, motivationLetter);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDto>> getApplications() {
        List<ApplicationDto> list = applicationService.getApplications();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable("id") Long id) {
        ApplicationDto dto = applicationService.getApplicationById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/score")
    public ResponseEntity<ScoreDto> getApplicationScoreById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(applicationService.getScoreByApplicationId(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<EmployeeDto> acceptApplication(@PathVariable("id") Long id) {
        return new ResponseEntity<>(applicationService.acceptByApplicationId(id), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto) {
        ApplicationDto updated = applicationService.updateApplication(applicationDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable("id") Long id) {
        applicationService.deleteApplicationById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/document-score")
    public ResponseEntity<SetDocumentScoreDto> setDocumentScore(@RequestBody SetDocumentScoreDto setDocumentScoreDto) {
        System.out.println("bobota");
        SetDocumentScoreDto updated = applicationService.setDocumentScore(setDocumentScoreDto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/assign-qcm")
    public ResponseEntity<AssignQcmDto> assignQcm(@RequestBody AssignQcmDto assignQcmDto) {
        AssignQcmDto updated = applicationService.assignQcm(assignQcmDto);
        return ResponseEntity.ok(updated);
    }
}

