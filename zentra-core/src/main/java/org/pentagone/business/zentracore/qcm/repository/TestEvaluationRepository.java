package org.pentagone.business.zentracore.qcm.repository;

import org.pentagone.business.zentracore.qcm.entity.TestEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestEvaluationRepository extends JpaRepository<TestEvaluation, Long> {
    
    List<TestEvaluation> findByIdCandidature(Long idCandidature);
    
    List<TestEvaluation> findByTypeTest(String typeTest);
    
    List<TestEvaluation> findByReussi(Boolean reussi);
}
