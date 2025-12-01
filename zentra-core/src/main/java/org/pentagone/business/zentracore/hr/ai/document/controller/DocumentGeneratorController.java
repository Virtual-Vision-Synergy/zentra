package org.pentagone.business.zentracore.hr.ai.document.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pentagone.business.zentracore.hr.ai.document.dto.DocumentGenerationRequest;
import org.pentagone.business.zentracore.hr.ai.document.dto.GeneratedDocumentDTO;
import org.pentagone.business.zentracore.hr.ai.document.service.DocumentGeneratorService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ai/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocumentGeneratorController {

    private final DocumentGeneratorService documentGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<GeneratedDocumentDTO> generateDocument(
            @RequestBody DocumentGenerationRequest request) {
        GeneratedDocumentDTO document = documentGeneratorService.generateDocument(request);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<GeneratedDocumentDTO>> getEmployeeDocuments(
            @PathVariable Integer employeeId) {
        List<GeneratedDocumentDTO> documents = documentGeneratorService.getEmployeeDocuments(employeeId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping
    public ResponseEntity<List<GeneratedDocumentDTO>> getAllDocuments() {
        List<GeneratedDocumentDTO> documents = documentGeneratorService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        // In a real implementation, fetch document from DB and return file
        // This is a simplified version
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document.pdf\"")
                .body(null);
    }

    @PostConstruct
    public void init() {
        log.info("========================================");
        log.info("✅ DocumentGeneratorController INITIALIZED");
        log.info("📍 Base path: /ai/documents");
        log.info("🌐 Full URL: http://localhost:8080/api/ai/documents");
        log.info("========================================");
    }
}
