package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.PerformanceCriterion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerformanceCriterionRepository extends JpaRepository<PerformanceCriterion, Long> {
    Optional<PerformanceCriterion> findByCode(String code);
}

