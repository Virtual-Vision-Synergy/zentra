package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.EmployeePerformanceEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeePerformanceEvaluationRepository extends JpaRepository<EmployeePerformanceEvaluation, Long> {
    Optional<EmployeePerformanceEvaluation> findByEmployeeIdAndPeriodId(Long employeeId, Long periodId);
    List<EmployeePerformanceEvaluation> findAllByEmployeeIdAndPeriodId(Long employeeId, Long periodId);
    List<EmployeePerformanceEvaluation> findByEmployeeId(Long employeeId);
    List<EmployeePerformanceEvaluation> findByPeriodId(Long periodId);
}

