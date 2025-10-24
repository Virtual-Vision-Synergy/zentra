package org.pentagone.business.zentracore.module.entretien.controller;

import lombok.RequiredArgsConstructor;
import org.pentagone.business.zentracore.module.entretien.entity.Entretien;
import org.pentagone.business.zentracore.module.entretien.service.EntretienService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/entretiens")
@RequiredArgsConstructor
public class EntretienController {
    
    private final EntretienService entretienService;
    
    @GetMapping
    public ResponseEntity<List<Entretien>> getAllEntretiens() {
        return ResponseEntity.ok(entretienService.getAllEntretiens());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Entretien> getEntretienById(@PathVariable Integer id) {
        return ResponseEntity.ok(entretienService.getEntretienById(id));
    }
    
    @GetMapping("/candidature/{idCandidature}")
    public ResponseEntity<List<Entretien>> getEntretiensByCandidature(@PathVariable Integer idCandidature) {
        return ResponseEntity.ok(entretienService.getEntretiensByCandidature(idCandidature));
    }
    
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Entretien>> getEntretiensByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(entretienService.getEntretiensByStatut(statut));
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Entretien>> getEntretiensByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(entretienService.getEntretiensByDate(date));
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<Entretien>> getEntretiensByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(entretienService.getEntretiensByDateRange(startDate, endDate));
    }
    
    @GetMapping("/type/{typeEntretien}")
    public ResponseEntity<List<Entretien>> getEntretiensByType(@PathVariable String typeEntretien) {
        return ResponseEntity.ok(entretienService.getEntretiensByType(typeEntretien));
    }
    
    @PostMapping
    public ResponseEntity<Entretien> createEntretien(@RequestBody Entretien entretien) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entretienService.createEntretien(entretien));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Entretien> updateEntretien(
            @PathVariable Integer id,
            @RequestBody Entretien entretien) {
        return ResponseEntity.ok(entretienService.updateEntretien(id, entretien));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntretien(@PathVariable Integer id) {
        entretienService.deleteEntretien(id);
        return ResponseEntity.noContent().build();
    }
}
