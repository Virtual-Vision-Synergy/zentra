package org.pentagone.business.zentracore.hr.ai.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pentagone.business.zentracore.hr.ai.chatbot.service.OpenAIService;
import org.pentagone.business.zentracore.hr.ai.recommendation.dto.CandidateRecommendationDTO;
import org.pentagone.business.zentracore.hr.ai.recommendation.entity.CandidateRecommendation;
import org.pentagone.business.zentracore.hr.ai.recommendation.repository.CandidateRecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateRecommendationService {

    private final CandidateRecommendationRepository recommendationRepository;
    private final OpenAIService openAIService;

    /**
     * Calculate match score between candidate and job
     */
    @Transactional
    public CandidateRecommendationDTO calculateMatch(Integer candidateId, Integer jobId,
                                                      Map<String, Object> candidateData,
                                                      Map<String, Object> jobData) {
        log.info("Calculating match for candidate {} and job {}", candidateId, jobId);

        try {
            // Extract skills
            List<String> candidateSkills = extractSkills(candidateData);
            List<String> requiredSkills = extractSkills(jobData);
            List<String> preferredSkills = extractPreferredSkills(jobData);

            // Calculate match score
            double skillMatch = calculateSkillMatch(candidateSkills, requiredSkills, preferredSkills);
            double experienceMatch = calculateExperienceMatch(candidateData, jobData);
            double educationMatch = calculateEducationMatch(candidateData, jobData);
            double locationMatch = calculateLocationMatch(candidateData, jobData);

            // Weighted average
            double finalScore = (skillMatch * 0.4) + (experienceMatch * 0.3) +
                               (educationMatch * 0.2) + (locationMatch * 0.1);

            String matchDetails = buildMatchDetails(skillMatch, experienceMatch, educationMatch,
                                                    locationMatch, candidateSkills, requiredSkills);

            // Save recommendation
            CandidateRecommendation recommendation = new CandidateRecommendation();
            recommendation.setCandidateId(candidateId);
            recommendation.setCandidateName((String) candidateData.get("name"));
            recommendation.setJobId(jobId);
            recommendation.setJobTitle((String) jobData.get("title"));
            recommendation.setMatchScore(finalScore);
            recommendation.setMatchDetails(matchDetails);
            recommendation.setCalculatedAt(LocalDateTime.now());

            CandidateRecommendation saved = recommendationRepository.save(recommendation);
            return convertToDTO(saved);

        } catch (Exception e) {
            log.error("Error calculating match", e);
            throw new RuntimeException("Erreur lors du calcul de correspondance: " + e.getMessage());
        }
    }

    /**
     * Get top candidates for a job
     */
    public List<CandidateRecommendationDTO> getTopCandidatesForJob(Integer jobId, int limit) {
        return recommendationRepository.findByJobIdOrderByMatchScoreDesc(jobId)
                .stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get recommended jobs for a candidate
     */
    public List<CandidateRecommendationDTO> getRecommendedJobsForCandidate(Integer candidateId) {
        return recommendationRepository.findByCandidateId(candidateId)
                .stream()
                .sorted((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Batch calculate matches for all candidates and a specific job
     */
    @Transactional
    public List<CandidateRecommendationDTO> calculateMatchesForJob(Integer jobId,
                                                                     Map<String, Object> jobData,
                                                                     List<Map<String, Object>> candidatesData) {
        List<CandidateRecommendationDTO> recommendations = new ArrayList<>();

        for (Map<String, Object> candidateData : candidatesData) {
            Integer candidateId = (Integer) candidateData.get("id");
            try {
                CandidateRecommendationDTO recommendation = calculateMatch(
                        candidateId, jobId, candidateData, jobData);
                recommendations.add(recommendation);
            } catch (Exception e) {
                log.error("Error calculating match for candidate {}", candidateId, e);
            }
        }

        return recommendations.stream()
                .sorted((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()))
                .collect(Collectors.toList());
    }

    /**
     * Extract skills from CV using AI
     */
    public List<String> extractSkillsFromCV(String cvText) {
        String skillsText = openAIService.extractSkillsFromCV(cvText);

        if (skillsText != null && !skillsText.isEmpty()) {
            return Arrays.stream(skillsText.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private List<String> extractSkills(Map<String, Object> data) {
        if (data.containsKey("skills")) {
            Object skills = data.get("skills");
            if (skills instanceof String) {
                return Arrays.stream(((String) skills).split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
            } else if (skills instanceof List) {
                return ((List<?>) skills).stream()
                        .map(Object::toString)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    private List<String> extractPreferredSkills(Map<String, Object> data) {
        if (data.containsKey("preferredSkills")) {
            Object skills = data.get("preferredSkills");
            if (skills instanceof String) {
                return Arrays.stream(((String) skills).split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
            } else if (skills instanceof List) {
                return ((List<?>) skills).stream()
                        .map(Object::toString)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    private double calculateSkillMatch(List<String> candidateSkills,
                                       List<String> requiredSkills,
                                       List<String> preferredSkills) {
        if (requiredSkills.isEmpty()) {
            return 0.5; // Neutral score if no requirements
        }

        // Count matching required skills
        long requiredMatches = requiredSkills.stream()
                .filter(candidateSkills::contains)
                .count();

        double requiredScore = (double) requiredMatches / requiredSkills.size();

        // Count matching preferred skills (bonus)
        if (!preferredSkills.isEmpty()) {
            long preferredMatches = preferredSkills.stream()
                    .filter(candidateSkills::contains)
                    .count();
            double preferredScore = (double) preferredMatches / preferredSkills.size();

            // 80% weight for required, 20% for preferred
            return (requiredScore * 0.8) + (preferredScore * 0.2);
        }

        return requiredScore;
    }

    private double calculateExperienceMatch(Map<String, Object> candidateData, Map<String, Object> jobData) {
        if (!candidateData.containsKey("yearsOfExperience") || !jobData.containsKey("requiredExperience")) {
            return 0.5;
        }

        int candidateExperience = ((Number) candidateData.get("yearsOfExperience")).intValue();
        int requiredExperience = ((Number) jobData.get("requiredExperience")).intValue();

        if (candidateExperience >= requiredExperience) {
            // Perfect match or overqualified
            int diff = candidateExperience - requiredExperience;
            if (diff <= 2) {
                return 1.0;
            } else if (diff <= 5) {
                return 0.9; // Slightly overqualified
            } else {
                return 0.7; // Significantly overqualified
            }
        } else {
            // Underqualified
            double ratio = (double) candidateExperience / requiredExperience;
            return Math.max(0, ratio);
        }
    }

    private double calculateEducationMatch(Map<String, Object> candidateData, Map<String, Object> jobData) {
        if (!candidateData.containsKey("education") || !jobData.containsKey("requiredEducation")) {
            return 0.5;
        }

        String candidateEducation = ((String) candidateData.get("education")).toLowerCase();
        String requiredEducation = ((String) jobData.get("requiredEducation")).toLowerCase();

        // Simple education level comparison
        Map<String, Integer> educationLevels = Map.of(
                "bac", 1,
                "bac+2", 2,
                "bac+3", 3,
                "licence", 3,
                "bac+5", 5,
                "master", 5,
                "doctorat", 8,
                "phd", 8
        );

        int candidateLevel = educationLevels.entrySet().stream()
                .filter(e -> candidateEducation.contains(e.getKey()))
                .mapToInt(Map.Entry::getValue)
                .max()
                .orElse(0);

        int requiredLevel = educationLevels.entrySet().stream()
                .filter(e -> requiredEducation.contains(e.getKey()))
                .mapToInt(Map.Entry::getValue)
                .max()
                .orElse(0);

        if (candidateLevel >= requiredLevel) {
            return 1.0;
        } else {
            return Math.max(0, (double) candidateLevel / requiredLevel);
        }
    }

    private double calculateLocationMatch(Map<String, Object> candidateData, Map<String, Object> jobData) {
        if (!candidateData.containsKey("location") || !jobData.containsKey("location")) {
            return 0.5;
        }

        String candidateLocation = ((String) candidateData.get("location")).toLowerCase();
        String jobLocation = ((String) jobData.get("location")).toLowerCase();

        // Check for remote work
        if (jobData.containsKey("remote") && (Boolean) jobData.get("remote")) {
            return 1.0;
        }

        // Exact match
        if (candidateLocation.equals(jobLocation)) {
            return 1.0;
        }

        // Same city/region (simplified check)
        String[] candidateTokens = candidateLocation.split(",");
        String[] jobTokens = jobLocation.split(",");

        for (String ct : candidateTokens) {
            for (String jt : jobTokens) {
                if (ct.trim().equals(jt.trim())) {
                    return 0.8;
                }
            }
        }

        return 0.3; // Different locations
    }

    private String buildMatchDetails(double skillMatch, double experienceMatch,
                                     double educationMatch, double locationMatch,
                                     List<String> candidateSkills, List<String> requiredSkills) {
        StringBuilder details = new StringBuilder();

        details.append("Compétences: ").append(String.format("%.0f%%", skillMatch * 100)).append("\n");
        details.append("Expérience: ").append(String.format("%.0f%%", experienceMatch * 100)).append("\n");
        details.append("Formation: ").append(String.format("%.0f%%", educationMatch * 100)).append("\n");
        details.append("Localisation: ").append(String.format("%.0f%%", locationMatch * 100)).append("\n");

        // Show matched skills
        List<String> matchedSkills = requiredSkills.stream()
                .filter(candidateSkills::contains)
                .collect(Collectors.toList());

        if (!matchedSkills.isEmpty()) {
            details.append("\nCompétences correspondantes: ").append(String.join(", ", matchedSkills));
        }

        List<String> missingSkills = requiredSkills.stream()
                .filter(skill -> !candidateSkills.contains(skill))
                .collect(Collectors.toList());

        if (!missingSkills.isEmpty()) {
            details.append("\nCompétences manquantes: ").append(String.join(", ", missingSkills));
        }

        return details.toString();
    }

    private CandidateRecommendationDTO convertToDTO(CandidateRecommendation recommendation) {
        CandidateRecommendationDTO dto = new CandidateRecommendationDTO();
        dto.setCandidateId(recommendation.getCandidateId());
        dto.setCandidateName(recommendation.getCandidateName());
        dto.setJobId(recommendation.getJobId());
        dto.setJobTitle(recommendation.getJobTitle());
        dto.setMatchScore(recommendation.getMatchScore());
        dto.setMatchDetails(recommendation.getMatchDetails());
        dto.setCalculatedAt(recommendation.getCalculatedAt());
        return dto;
    }
}

