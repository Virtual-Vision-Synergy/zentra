package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.QcmDto;
import org.pentagone.business.zentracore.hr.service.QcmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qcms")
public class QcmController {

    private final QcmService qcmService;

    public QcmController(QcmService qcmService) {
        this.qcmService = qcmService;
    }

    @PostMapping
    public ResponseEntity<QcmDto> createQcm(@RequestBody QcmDto qcmDto) {
        QcmDto created = qcmService.createQcm(qcmDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<QcmDto> updateQcm(@RequestBody QcmDto qcmDto) {
        QcmDto updated = qcmService.updateQcm(qcmDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QcmDto> getQcmById(@PathVariable Long id) {
        QcmDto qcm = qcmService.getQcmById(id);
        return new ResponseEntity<>(qcm, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QcmDto>> getQcms() {
        List<QcmDto> qcms = qcmService.getQcms();
        return new ResponseEntity<>(qcms, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQcmById(@PathVariable Long id) {
        qcmService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
