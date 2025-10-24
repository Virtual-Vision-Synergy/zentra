package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.EmploymentContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmploymentContractRepository extends JpaRepository<EmploymentContract, Long> {
    List<EmploymentContract> findByEmployeeId(Long employeeId);
    Optional<EmploymentContract> findByContractNumber(String contractNumber);
    boolean existsByContractNumber(String contractNumber);
}
