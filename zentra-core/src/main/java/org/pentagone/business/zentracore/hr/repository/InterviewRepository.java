package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationId(Long applicationId);
    List<Interview> findByInterviewerId(Long interviewerId);
    List<Interview> findByInterviewType(String interviewType);
    List<Interview> findByCandidateId(Long candidateId);
    List<Interview> findByStatus(String status);
    List<Interview> findByInterviewDate(LocalDate interviewDate);
    List<Interview> findByInterviewDateBetween(LocalDate startDate, LocalDate endDate);
    List<Interview> findByInterviewerIdAndStatus(Long interviewerId, String status);
    List<Interview> findByInterviewTypeAndStatus(String interviewType, String status);
}
