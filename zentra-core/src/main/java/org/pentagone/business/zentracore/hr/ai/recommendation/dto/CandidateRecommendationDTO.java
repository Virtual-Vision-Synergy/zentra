package org.pentagone.business.zentracore.hr.ai.recommendation.dto;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CandidateRecommendationDTO {
    private java.time.LocalDateTime calculatedAt;
    private String matchDetails;
    private Double matchScore; // 0.0 to 1.0
    private String jobTitle;
    private Integer jobId;
    private String candidateName;
    private Integer candidateId;
}





