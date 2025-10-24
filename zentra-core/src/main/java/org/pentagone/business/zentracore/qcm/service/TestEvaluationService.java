package org.pentagone.business.zentracore.qcm.service;

import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.qcm.dto.TestEvaluationDTO;
import org.pentagone.business.zentracore.qcm.entity.TestEvaluation;
import org.pentagone.business.zentracore.qcm.repository.TestEvaluationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestEvaluationService {

    private final TestEvaluationRepository testEvaluationRepository;

    public TestEvaluationService(TestEvaluationRepository testEvaluationRepository) {
        this.testEvaluationRepository = testEvaluationRepository;
    }

    public List<TestEvaluationDTO> getAllTests() {
        return testEvaluationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TestEvaluationDTO getTestById(Long id) {
        TestEvaluation test = testEvaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test d'évaluation non trouvé avec l'ID: " + id));
        return convertToDTO(test);
    }

    public List<TestEvaluationDTO> getTestsByCandidature(Long idCandidature) {
        return testEvaluationRepository.findByIdCandidature(idCandidature).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TestEvaluationDTO> getTestsByType(String typeTest) {
        return testEvaluationRepository.findByTypeTest(typeTest).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TestEvaluationDTO> getTestsByReussi(Boolean reussi) {
        return testEvaluationRepository.findByReussi(reussi).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TestEvaluationDTO createTest(TestEvaluationDTO testDTO) {
        TestEvaluation test = convertToEntity(testDTO);
        TestEvaluation savedTest = testEvaluationRepository.save(test);
        return convertToDTO(savedTest);
    }

    public TestEvaluationDTO updateTest(Long id, TestEvaluationDTO testDTO) {
        TestEvaluation existingTest = testEvaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test d'évaluation non trouvé avec l'ID: " + id));
        
        updateEntityFromDTO(existingTest, testDTO);
        TestEvaluation updatedTest = testEvaluationRepository.save(existingTest);
        return convertToDTO(updatedTest);
    }

    public void deleteTest(Long id) {
        if (!testEvaluationRepository.existsById(id)) {
            throw new EntityNotFoundException("Test d'évaluation non trouvé avec l'ID: " + id);
        }
        testEvaluationRepository.deleteById(id);
    }

    private TestEvaluationDTO convertToDTO(TestEvaluation test) {
        TestEvaluationDTO dto = new TestEvaluationDTO();
        dto.setIdTest(test.getIdTest());
        dto.setIdCandidature(test.getIdCandidature());
        dto.setTypeTest(test.getTypeTest());
        dto.setNomTest(test.getNomTest());
        dto.setDescription(test.getDescription());
        dto.setDateTest(test.getDateTest());
        dto.setHeureDebut(test.getHeureDebut());
        dto.setHeureFin(test.getHeureFin());
        dto.setDureeMinutes(test.getDureeMinutes());
        dto.setScoreObtenu(test.getScoreObtenu());
        dto.setScoreRequis(test.getScoreRequis());
        dto.setReussi(test.getReussi());
        dto.setCommentaire(test.getCommentaire());
        dto.setEvaluateur(test.getEvaluateur());
        return dto;
    }

    private TestEvaluation convertToEntity(TestEvaluationDTO dto) {
        TestEvaluation test = new TestEvaluation();
        test.setIdCandidature(dto.getIdCandidature());
        test.setTypeTest(dto.getTypeTest());
        test.setNomTest(dto.getNomTest());
        test.setDescription(dto.getDescription());
        test.setDateTest(dto.getDateTest());
        test.setHeureDebut(dto.getHeureDebut());
        test.setHeureFin(dto.getHeureFin());
        test.setDureeMinutes(dto.getDureeMinutes());
        test.setScoreObtenu(dto.getScoreObtenu());
        test.setScoreRequis(dto.getScoreRequis());
        test.setReussi(dto.getReussi());
        test.setCommentaire(dto.getCommentaire());
        test.setEvaluateur(dto.getEvaluateur());
        return test;
    }

    private void updateEntityFromDTO(TestEvaluation test, TestEvaluationDTO dto) {
        if (dto.getIdCandidature() != null) {
            test.setIdCandidature(dto.getIdCandidature());
        }
        if (dto.getTypeTest() != null) {
            test.setTypeTest(dto.getTypeTest());
        }
        if (dto.getNomTest() != null) {
            test.setNomTest(dto.getNomTest());
        }
        if (dto.getDescription() != null) {
            test.setDescription(dto.getDescription());
        }
        if (dto.getDateTest() != null) {
            test.setDateTest(dto.getDateTest());
        }
        if (dto.getHeureDebut() != null) {
            test.setHeureDebut(dto.getHeureDebut());
        }
        if (dto.getHeureFin() != null) {
            test.setHeureFin(dto.getHeureFin());
        }
        if (dto.getDureeMinutes() != null) {
            test.setDureeMinutes(dto.getDureeMinutes());
        }
        if (dto.getScoreObtenu() != null) {
            test.setScoreObtenu(dto.getScoreObtenu());
        }
        if (dto.getScoreRequis() != null) {
            test.setScoreRequis(dto.getScoreRequis());
        }
        if (dto.getReussi() != null) {
            test.setReussi(dto.getReussi());
        }
        if (dto.getCommentaire() != null) {
            test.setCommentaire(dto.getCommentaire());
        }
        if (dto.getEvaluateur() != null) {
            test.setEvaluateur(dto.getEvaluateur());
        }
    }
}
