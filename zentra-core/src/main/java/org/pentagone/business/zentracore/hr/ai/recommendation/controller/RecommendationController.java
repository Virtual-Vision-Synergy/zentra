package org.pentagone.business.zentracore.hr.ai.recommendation.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pentagone.business.zentracore.hr.ai.recommendation.dto.CandidateRecommendationDTO;
import org.pentagone.business.zentracore.hr.ai.recommendation.service.CandidateRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ai/recommendation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final CandidateRecommendationService recommendationService;

    @PostConstruct
    public void init() {
        log.info("========================================");
        log.info("✅ RecommendationController INITIALIZED");
        log.info("📍 Base path: /ai/recommendation");
        log.info("🌐 Full URL: http://localhost:8080/api/ai/recommendation");
        log.info("========================================");
    }

    @PostMapping("/match")
    public ResponseEntity<CandidateRecommendationDTO> calculateMatch(
            @RequestParam Integer candidateId,
            @RequestParam Integer jobId,
            @RequestBody Map<String, Object> data) {

        Map<String, Object> candidateData = (Map<String, Object>) data.get("candidate");
        Map<String, Object> jobData = (Map<String, Object>) data.get("job");

        CandidateRecommendationDTO recommendation = recommendationService.calculateMatch(
                candidateId, jobId, candidateData, jobData);

        return ResponseEntity.ok(recommendation);
    }

    @GetMapping("/job/{jobId}/top-candidates")
    public ResponseEntity<List<CandidateRecommendationDTO>> getTopCandidates(
            @PathVariable Integer jobId,
            @RequestParam(defaultValue = "10") int limit) {

        List<CandidateRecommendationDTO> candidates =
                recommendationService.getTopCandidatesForJob(jobId, limit);

        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/candidate/{candidateId}/recommended-jobs")
    public ResponseEntity<List<CandidateRecommendationDTO>> getRecommendedJobs(
            @PathVariable Integer candidateId) {

        List<CandidateRecommendationDTO> jobs =
                recommendationService.getRecommendedJobsForCandidate(candidateId);

        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/job/{jobId}/batch-match")
    public ResponseEntity<List<CandidateRecommendationDTO>> batchCalculateMatches(
            @PathVariable Integer jobId,
            @RequestBody Map<String, Object> data) {

        Map<String, Object> jobData = (Map<String, Object>) data.get("job");
        List<Map<String, Object>> candidatesData =
                (List<Map<String, Object>>) data.get("candidates");

        List<CandidateRecommendationDTO> recommendations =
                recommendationService.calculateMatchesForJob(jobId, jobData, candidatesData);

        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/extract-skills")
    public ResponseEntity<List<String>> extractSkillsFromCV(@RequestBody Map<String, String> data) {
        String cvText = data.get("cvText");
        List<String> skills = recommendationService.extractSkillsFromCV(cvText);
        return ResponseEntity.ok(skills);
    }
}
