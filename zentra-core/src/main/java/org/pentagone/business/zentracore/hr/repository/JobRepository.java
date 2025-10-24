package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByDepartmentId(Long departmentId);
    List<Job> findByTitleContainingIgnoreCase(String title);
}
