package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    Optional<Employee> findByWorkEmail(String workEmail);
    List<Employee> findByJobId(Long jobId);
    List<Employee> findByCandidateId(Long candidateId);
    boolean existsByEmployeeNumber(String employeeNumber);
    boolean existsByWorkEmail(String workEmail);
}
