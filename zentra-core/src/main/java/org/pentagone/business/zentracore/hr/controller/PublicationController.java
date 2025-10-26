package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.PublicationDto;
import org.pentagone.business.zentracore.hr.service.PublicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publications")
public class PublicationController {

    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping
    public ResponseEntity<PublicationDto> create(@RequestBody PublicationDto dto) {
        PublicationDto created = publicationService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PublicationDto> update(@RequestBody PublicationDto dto) {
        PublicationDto updated = publicationService.update(dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PublicationDto>> findAll() {
        return ResponseEntity.ok(publicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publicationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

