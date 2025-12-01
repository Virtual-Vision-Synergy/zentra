package org.pentagone.business.zentracore.hr.ai.prediction.repository;

import org.pentagone.business.zentracore.hr.ai.prediction.entity.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {
    List<Anomaly> findByResolvedFalseOrderByDetectedAtDesc();
    List<Anomaly> findByEmployeeId(Integer employeeId);
    List<Anomaly> findByAnomalyType(String anomalyType);
    List<Anomaly> findBySeverity(String severity);
}

