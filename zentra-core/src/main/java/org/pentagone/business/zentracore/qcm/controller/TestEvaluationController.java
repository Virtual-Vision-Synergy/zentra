package org.pentagone.business.zentracore.qcm.controller;

import org.pentagone.business.zentracore.qcm.dto.TestEvaluationDTO;
import org.pentagone.business.zentracore.qcm.service.TestEvaluationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests-evaluation")
@CrossOrigin(origins = "*")
public class TestEvaluationController {

    private final TestEvaluationService testEvaluationService;

    public TestEvaluationController(TestEvaluationService testEvaluationService) {
        this.testEvaluationService = testEvaluationService;
    }

    @GetMapping
    public ResponseEntity<List<TestEvaluationDTO>> getAllTests() {
        List<TestEvaluationDTO> tests = testEvaluationService.getAllTests();
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestEvaluationDTO> getTestById(@PathVariable Long id) {
        TestEvaluationDTO test = testEvaluationService.getTestById(id);
        return ResponseEntity.ok(test);
    }

    @GetMapping("/candidature/{idCandidature}")
    public ResponseEntity<List<TestEvaluationDTO>> getTestsByCandidature(@PathVariable Long idCandidature) {
        List<TestEvaluationDTO> tests = testEvaluationService.getTestsByCandidature(idCandidature);
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/type/{typeTest}")
    public ResponseEntity<List<TestEvaluationDTO>> getTestsByType(@PathVariable String typeTest) {
        List<TestEvaluationDTO> tests = testEvaluationService.getTestsByType(typeTest);
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/reussi/{reussi}")
    public ResponseEntity<List<TestEvaluationDTO>> getTestsByReussi(@PathVariable Boolean reussi) {
        List<TestEvaluationDTO> tests = testEvaluationService.getTestsByReussi(reussi);
        return ResponseEntity.ok(tests);
    }

    @PostMapping
    public ResponseEntity<TestEvaluationDTO> createTest(@RequestBody TestEvaluationDTO testDTO) {
        TestEvaluationDTO createdTest = testEvaluationService.createTest(testDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestEvaluationDTO> updateTest(@PathVariable Long id, @RequestBody TestEvaluationDTO testDTO) {
        TestEvaluationDTO updatedTest = testEvaluationService.updateTest(id, testDTO);
        return ResponseEntity.ok(updatedTest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testEvaluationService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }
}
