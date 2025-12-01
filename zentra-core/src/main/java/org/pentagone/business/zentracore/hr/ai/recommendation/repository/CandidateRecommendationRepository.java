package org.pentagone.business.zentracore.hr.ai.recommendation.repository;

import org.pentagone.business.zentracore.hr.ai.recommendation.entity.CandidateRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRecommendationRepository extends JpaRepository<CandidateRecommendation, Long> {
    List<CandidateRecommendation> findByJobIdOrderByMatchScoreDesc(Integer jobId);
    List<CandidateRecommendation> findByCandidateId(Integer candidateId);
    List<CandidateRecommendation> findByMatchScoreGreaterThanEqualOrderByMatchScoreDesc(Double threshold);
}

