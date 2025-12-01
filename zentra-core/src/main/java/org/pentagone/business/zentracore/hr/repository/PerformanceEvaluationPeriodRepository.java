package org.pentagone.business.zentracore.hr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.pentagone.business.zentracore.hr.entity.PerformanceEvaluationPeriod;

public interface PerformanceEvaluationPeriodRepository extends JpaRepository<PerformanceEvaluationPeriod, Long> {
    Optional<PerformanceEvaluationPeriod> findByCode(String code);
}



