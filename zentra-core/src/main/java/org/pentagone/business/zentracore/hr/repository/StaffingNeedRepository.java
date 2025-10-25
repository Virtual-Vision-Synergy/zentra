package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.StaffingNeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffingNeedRepository extends JpaRepository<StaffingNeed, Long> {
    List<StaffingNeed> findByDepartmentId(Long departmentId);
    List<StaffingNeed> findByJobId(Long jobId);
    List<StaffingNeed> findByStatus(String status);
    List<StaffingNeed> findByPriority(String priority);
}
