package org.pentagone.business.zentracore.besoin.controller;

import org.pentagone.business.zentracore.besoin.dto.BesoinRequestDTO;
import org.pentagone.business.zentracore.besoin.dto.BesoinResponseDTO;
import org.pentagone.business.zentracore.besoin.service.BesoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/besoins")
public class BesoinController {
    private final BesoinService besoinService;

    public BesoinController(BesoinService besoinService) {
        this.besoinService = besoinService;
    }

    @GetMapping
    public ResponseEntity<List<BesoinResponseDTO>> getAllBesoins(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String departement
    ) {
        if (statut != null) {
            return ResponseEntity.ok(besoinService.getBesoinsByStatut(statut));
        }
        if (departement != null) {
            return ResponseEntity.ok(besoinService.getBesoinsByDepartement(departement));
        }
        return ResponseEntity.ok(besoinService.getAllBesoins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BesoinResponseDTO> getBesoinById(@PathVariable Long id) {
        return ResponseEntity.ok(besoinService.getBesoinById(id));
    }

    @PostMapping
    public ResponseEntity<BesoinResponseDTO> createBesoin(@RequestBody BesoinRequestDTO requestDTO) {
        BesoinResponseDTO createdBesoin = besoinService.createBesoin(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBesoin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BesoinResponseDTO> updateBesoin(
            @PathVariable Long id,
            @RequestBody BesoinRequestDTO requestDTO
    ) {
        BesoinResponseDTO updatedBesoin = besoinService.updateBesoin(id, requestDTO);
        return ResponseEntity.ok(updatedBesoin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBesoin(@PathVariable Long id) {
        besoinService.deleteBesoin(id);
        return ResponseEntity.noContent().build();
    }
}
