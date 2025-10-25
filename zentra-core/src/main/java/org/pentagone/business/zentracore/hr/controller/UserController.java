package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.AttemptDto;
import org.pentagone.business.zentracore.hr.dto.AuthRequestDto;
import org.pentagone.business.zentracore.hr.dto.CandidateMinInfoDto;
import org.pentagone.business.zentracore.hr.dto.QcmDto;
import org.pentagone.business.zentracore.hr.service.AttemptService;
import org.pentagone.business.zentracore.hr.service.CandidateService;
import org.pentagone.business.zentracore.hr.service.QcmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CandidateService candidateService;
    private final QcmService qcmService;
    private final AttemptService attemptService;

    public UserController(CandidateService candidateService, QcmService qcmService, AttemptService attemptService) {
        this.candidateService = candidateService;
        this.qcmService = qcmService;
        this.attemptService = attemptService;
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
}
