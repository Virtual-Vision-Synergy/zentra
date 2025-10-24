package org.pentagone.business.zentracore.candidature.controller;

import lombok.RequiredArgsConstructor;
import org.pentagone.business.zentracore.candidature.dto.CandidatureCreateDto;
import org.pentagone.business.zentracore.candidature.dto.CandidatureResponseDto;
import org.pentagone.business.zentracore.candidature.dto.CandidatureUpdateDto;
import org.pentagone.business.zentracore.candidature.service.CandidatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidatures")
@RequiredArgsConstructor
public class CandidatureController {

    private final CandidatureService candidatureService;

    @PostMapping
    public ResponseEntity<CandidatureResponseDto> createCandidature(@RequestBody CandidatureCreateDto createDto) {
        CandidatureResponseDto response = candidatureService.createCandidature(createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatureResponseDto> getCandidatureById(@PathVariable Long id) {
        CandidatureResponseDto response = candidatureService.getCandidatureById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CandidatureResponseDto>> getAllCandidatures(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) Long candidatId,
            @RequestParam(required = false) Long annonceId) {
        
        List<CandidatureResponseDto> response;
        
        if (statut != null) {
            response = candidatureService.getCandidaturesByStatut(statut);
        } else if (candidatId != null) {
            response = candidatureService.getCandidaturesByCandidat(candidatId);
        } else if (annonceId != null) {
            response = candidatureService.getCandidaturesByAnnonce(annonceId);
        } else {
            response = candidatureService.getAllCandidatures();
        }
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidatureResponseDto> updateCandidature(
            @PathVariable Long id,
            @RequestBody CandidatureUpdateDto updateDto) {
        CandidatureResponseDto response = candidatureService.updateCandidature(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidature(@PathVariable Long id) {
        candidatureService.deleteCandidature(id);
        return ResponseEntity.noContent().build();
    }
}
