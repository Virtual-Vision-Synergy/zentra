package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByQcmId(Long qcmId);
    List<Attempt> findByApplicationId(Long applicationId);
}
