package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.*;
import org.pentagone.business.zentracore.hr.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CandidateService candidateService;
    private final QcmService qcmService;
    private final AttemptService attemptService;
    private final PublicationService publicationService;
    private final ApplicationService applicationService;

    public UserController(CandidateService candidateService,
                          QcmService qcmService,
                          AttemptService attemptService,
                          PublicationService publicationService,
                          ApplicationService applicationService) {
        this.candidateService = candidateService;
        this.qcmService = qcmService;
        this.attemptService = attemptService;
        this.publicationService = publicationService;
        this.applicationService = applicationService;
    }

    @PostMapping("/auth")
    public ResponseEntity<CandidateMinInfoDto> authenticateByToken(@RequestBody AuthRequestDto authRequestDto) {
        CandidateMinInfoDto candidate = candidateService.getCandidateByTokenValue(authRequestDto.getToken());
        return new ResponseEntity<>(candidate, HttpStatus.OK);
    }

    @GetMapping("/qcm")
    public ResponseEntity<QcmDto> getQcm(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        CandidateMinInfoDto candidate = candidateService.getCandidateByTokenValue(token);
        QcmDto qcmDto = qcmService.getQcmByApplicationId(candidate.getApplicationId());
        return new ResponseEntity<>(qcmDto, HttpStatus.OK);
    }

    @PostMapping("/attempt")
    public ResponseEntity<AttemptDto> createAttempt(@RequestHeader("Authorization") String bearerToken, @RequestBody AttemptDto attemptDto) {
        String token = bearerToken.replace("Bearer ", "");
        CandidateMinInfoDto candidate = candidateService.getCandidateByTokenValue(token);
        attemptDto.setApplicationId(candidate.getApplicationId());
        AttemptDto createdAttempt = attemptService.createAttempt(attemptDto);
        return new ResponseEntity<>(createdAttempt, HttpStatus.CREATED);
    }

    @GetMapping("/publications")
    public ResponseEntity<List<PublicationDto>> getPublications() {
        List<PublicationDto> publications = publicationService.getOpenPublications();
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationDto> createApplication(
            @RequestPart("application") ApplicationDto applicationDto,
            @RequestPart(value = "cv") MultipartFile cv,
            @RequestPart(value = "motivationLetter") MultipartFile motivationLetter
    ) {
        ApplicationDto created = applicationService.createApplication(applicationDto, cv, motivationLetter);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
