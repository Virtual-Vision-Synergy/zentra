package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.CandidateDto;
import org.pentagone.business.zentracore.hr.service.CandidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<CandidateDto> createCandidate(@RequestBody CandidateDto candidateDto) {
        CandidateDto created = candidateService.createCandidate(candidateDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CandidateDto> updateCandidate(@RequestBody CandidateDto candidateDto) {
        CandidateDto updated = candidateService.updateCandidate(candidateDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDto> getCandidateById(@PathVariable Long id) {
        CandidateDto candidate = candidateService.getCandidateById(id);
        return new ResponseEntity<>(candidate, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CandidateDto>> getAllCandidates() {
        List<CandidateDto> candidates = candidateService.getAllCandidates();
        return new ResponseEntity<>(candidates, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidateById(@PathVariable Long id) {
        candidateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

