package org.pentagone.business.zentracore.hr.ai.prediction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pentagone.business.zentracore.hr.ai.prediction.dto.TurnoverPredictionDTO;
import org.pentagone.business.zentracore.hr.ai.prediction.entity.TurnoverPrediction;
import org.pentagone.business.zentracore.hr.ai.prediction.repository.TurnoverPredictionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TurnoverPredictionService {

    private final TurnoverPredictionRepository predictionRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${ai.ml-service.url:http://localhost:5000}")
    private String mlServiceUrl;

    @Value("${ai.ml-service.enabled:false}")
    private boolean mlServiceEnabled;

    /**
     * Predicts turnover risk for a specific employee
     */
    @Transactional
    public TurnoverPredictionDTO predictTurnover(Integer employeeId, Map<String, Object> employeeData) {
        log.info("Predicting turnover for employee: {}", employeeId);

        try {
            Double riskScore;
            String reasons;

            if (mlServiceEnabled) {
                // Call external ML service
                riskScore = callMLService(employeeData);
                reasons = "Analyse ML basée sur l'historique et les patterns";
            } else {
                // Use rule-based prediction
                riskScore = calculateRuleBasedRisk(employeeData);
                reasons = buildReasonsFromData(employeeData);
            }

            String riskLevel = determineRiskLevel(riskScore);

            // Save prediction
            TurnoverPrediction prediction = new TurnoverPrediction();
            prediction.setEmployeeId(employeeId);
            prediction.setEmployeeName((String) employeeData.getOrDefault("name", "Unknown"));
            prediction.setRiskScore(riskScore);
            prediction.setRiskLevel(riskLevel);
            prediction.setReasons(reasons);
            prediction.setPredictedAt(LocalDateTime.now());

            TurnoverPrediction saved = predictionRepository.save(prediction);
            return convertToDTO(saved);

        } catch (Exception e) {
            log.error("Error predicting turnover", e);
            throw new RuntimeException("Erreur lors de la prédiction: " + e.getMessage());
        }
    }

    /**
     * Rule-based risk calculation when ML service is not available
     */
    private Double calculateRuleBasedRisk(Map<String, Object> data) {
        double risk = 0.0;
        int factors = 0;

        // Factor 1: Tenure (less time = higher risk)
        if (data.containsKey("tenureMonths")) {
            int tenure = (Integer) data.get("tenureMonths");
            if (tenure < 6) {
                risk += 0.3;
            } else if (tenure < 12) {
                risk += 0.2;
            } else if (tenure < 24) {
                risk += 0.1;
            }
            factors++;
        }

        // Factor 2: Performance score (lower = higher risk)
        if (data.containsKey("performanceScore")) {
            double performance = ((Number) data.get("performanceScore")).doubleValue();
            if (performance < 2.0) {
                risk += 0.3;
            } else if (performance < 3.0) {
                risk += 0.2;
            }
            factors++;
        }

        // Factor 3: Absence rate
        if (data.containsKey("absenceRate")) {
            double absenceRate = ((Number) data.get("absenceRate")).doubleValue();
            if (absenceRate > 10.0) {
                risk += 0.3;
            } else if (absenceRate > 5.0) {
                risk += 0.15;
            }
            factors++;
        }

        // Factor 4: Recent salary increase
        if (data.containsKey("monthsSinceLastRaise")) {
            int monthsSinceRaise = (Integer) data.get("monthsSinceLastRaise");
            if (monthsSinceRaise > 24) {
                risk += 0.2;
            } else if (monthsSinceRaise > 12) {
                risk += 0.1;
            }
            factors++;
        }

        // Factor 5: Training hours (less training = higher risk)
        if (data.containsKey("trainingHoursLastYear")) {
            int trainingHours = (Integer) data.get("trainingHoursLastYear");
            if (trainingHours < 10) {
                risk += 0.15;
            }
            factors++;
        }

        // Normalize the risk score
        return Math.min(1.0, factors > 0 ? risk / Math.sqrt(factors) : 0.5);
    }

    private String buildReasonsFromData(Map<String, Object> data) {
        List<String> reasons = new ArrayList<>();

        if (data.containsKey("tenureMonths")) {
            int tenure = (Integer) data.get("tenureMonths");
            if (tenure < 12) {
                reasons.add("Ancienneté faible (" + tenure + " mois)");
            }
        }

        if (data.containsKey("absenceRate")) {
            double absenceRate = ((Number) data.get("absenceRate")).doubleValue();
            if (absenceRate > 5.0) {
                reasons.add("Taux d'absence élevé (" + String.format("%.1f", absenceRate) + "%)");
            }
        }

        if (data.containsKey("performanceScore")) {
            double performance = ((Number) data.get("performanceScore")).doubleValue();
            if (performance < 3.0) {
                reasons.add("Performance sous la moyenne (" + String.format("%.1f", performance) + "/5)");
            }
        }

        if (data.containsKey("monthsSinceLastRaise")) {
            int monthsSinceRaise = (Integer) data.get("monthsSinceLastRaise");
            if (monthsSinceRaise > 12) {
                reasons.add("Pas d'augmentation depuis " + monthsSinceRaise + " mois");
            }
        }

        return reasons.isEmpty() ? "Analyse des données employé" : String.join("; ", reasons);
    }

    private Double callMLService(Map<String, Object> data) {
        try {
            WebClient webClient = webClientBuilder.baseUrl(mlServiceUrl).build();

            Map<String, Object> response = webClient.post()
                    .uri("/api/predict/turnover")
                    .bodyValue(data)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null ? ((Number) response.get("riskScore")).doubleValue() : 0.5;
        } catch (Exception e) {
            log.error("Error calling ML service", e);
            return calculateRuleBasedRisk(data); // Fallback
        }
    }

    private String determineRiskLevel(Double score) {
        if (score >= 0.7) return "HIGH";
        if (score >= 0.4) return "MEDIUM";
        return "LOW";
    }

    public List<TurnoverPredictionDTO> getHighRiskEmployees() {
        return predictionRepository.findByRiskScoreGreaterThanEqualOrderByRiskScoreDesc(0.7)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TurnoverPredictionDTO getLatestPrediction(Integer employeeId) {
        return predictionRepository.findTopByEmployeeIdOrderByPredictedAtDesc(employeeId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<TurnoverPredictionDTO> getAllPredictions() {
        return predictionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TurnoverPredictionDTO convertToDTO(TurnoverPrediction prediction) {
        TurnoverPredictionDTO dto = new TurnoverPredictionDTO();
        dto.setEmployeeId(prediction.getEmployeeId());
        dto.setEmployeeName(prediction.getEmployeeName());
        dto.setRiskScore(prediction.getRiskScore());
        dto.setRiskLevel(prediction.getRiskLevel());
        dto.setReasons(prediction.getReasons());
        dto.setPredictedAt(prediction.getPredictedAt());
        return dto;
    }
}

