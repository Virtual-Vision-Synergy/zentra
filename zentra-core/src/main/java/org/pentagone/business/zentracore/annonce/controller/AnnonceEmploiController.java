package org.pentagone.business.zentracore.annonce.controller;

import lombok.RequiredArgsConstructor;
import org.pentagone.business.zentracore.annonce.entity.AnnonceEmploi;
import org.pentagone.business.zentracore.annonce.service.AnnonceEmploiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/annonces")
@RequiredArgsConstructor
public class AnnonceEmploiController {

    private final AnnonceEmploiService annonceEmploiService;

    @GetMapping
    public ResponseEntity<List<AnnonceEmploi>> getAllAnnonces() {
        return ResponseEntity.ok(annonceEmploiService.getAllAnnonces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceEmploi> getAnnonceById(@PathVariable Integer id) {
        return ResponseEntity.ok(annonceEmploiService.getAnnonceById(id));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<AnnonceEmploi>> getAnnoncesByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(annonceEmploiService.getAnnoncesByStatut(statut));
    }

    @GetMapping("/poste/{idPoste}")
    public ResponseEntity<List<AnnonceEmploi>> getAnnoncesByPoste(@PathVariable Integer idPoste) {
        return ResponseEntity.ok(annonceEmploiService.getAnnoncesByPoste(idPoste));
    }

    @PostMapping
    public ResponseEntity<AnnonceEmploi> createAnnonce(@RequestBody AnnonceEmploi annonce) {
        AnnonceEmploi createdAnnonce = annonceEmploiService.createAnnonce(annonce);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnnonce);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceEmploi> updateAnnonce(
            @PathVariable Integer id,
            @RequestBody AnnonceEmploi annonceDetails) {
        return ResponseEntity.ok(annonceEmploiService.updateAnnonce(id, annonceDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Integer id) {
        annonceEmploiService.deleteAnnonce(id);
        return ResponseEntity.noContent().build();
    }
}
