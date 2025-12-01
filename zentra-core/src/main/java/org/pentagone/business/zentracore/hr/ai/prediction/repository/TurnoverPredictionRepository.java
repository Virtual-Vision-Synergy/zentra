package org.pentagone.business.zentracore.hr.ai.prediction.repository;

import org.pentagone.business.zentracore.hr.ai.prediction.entity.TurnoverPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoverPredictionRepository extends JpaRepository<TurnoverPrediction, Long> {
    Optional<TurnoverPrediction> findTopByEmployeeIdOrderByPredictedAtDesc(Integer employeeId);
    List<TurnoverPrediction> findByRiskLevel(String riskLevel);
    List<TurnoverPrediction> findByRiskScoreGreaterThanEqualOrderByRiskScoreDesc(Double threshold);
}

