package org.pentagone.business.zentracore.hr.ai.prediction.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pentagone.business.zentracore.hr.ai.prediction.dto.AnomalyDTO;
import org.pentagone.business.zentracore.hr.ai.prediction.dto.TurnoverPredictionDTO;
import org.pentagone.business.zentracore.hr.ai.prediction.service.AnomalyDetectionService;
import org.pentagone.business.zentracore.hr.ai.prediction.service.TurnoverPredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ai/prediction")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PredictionController {

    private final TurnoverPredictionService turnoverService;
    private final AnomalyDetectionService anomalyService;

    @PostConstruct
    public void init() {
        log.info("========================================");
        log.info("✅ PredictionController INITIALIZED (hr.ai package)");
        log.info("📍 Base path: /ai/prediction");
        log.info("🌐 Full URL: http://localhost:8080/api/ai/prediction");
        log.info("========================================");
    }

    // Turnover prediction endpoints
    @PostMapping("/turnover/{employeeId}")
    public ResponseEntity<TurnoverPredictionDTO> predictTurnover(
            @PathVariable Integer employeeId,
            @RequestBody Map<String, Object> employeeData) {
        TurnoverPredictionDTO prediction = turnoverService.predictTurnover(employeeId, employeeData);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping("/turnover/high-risk")
    public ResponseEntity<List<TurnoverPredictionDTO>> getHighRiskEmployees() {
        List<TurnoverPredictionDTO> predictions = turnoverService.getHighRiskEmployees();
        return ResponseEntity.ok(predictions);
    }

    @GetMapping("/turnover/employee/{employeeId}")
    public ResponseEntity<TurnoverPredictionDTO> getEmployeePrediction(@PathVariable Integer employeeId) {
        TurnoverPredictionDTO prediction = turnoverService.getLatestPrediction(employeeId);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping("/turnover/all")
    public ResponseEntity<List<TurnoverPredictionDTO>> getAllPredictions() {
        List<TurnoverPredictionDTO> predictions = turnoverService.getAllPredictions();
        return ResponseEntity.ok(predictions);
    }

    // Anomaly detection endpoints
    @PostMapping("/anomaly/attendance")
    public ResponseEntity<List<AnomalyDTO>> detectAttendanceAnomalies(
            @RequestBody List<Map<String, Object>> attendanceData) {
        List<AnomalyDTO> anomalies = anomalyService.detectAttendanceAnomalies(attendanceData);
        return ResponseEntity.ok(anomalies);
    }

    @PostMapping("/anomaly/payroll")
    public ResponseEntity<List<AnomalyDTO>> detectPayrollAnomalies(
            @RequestBody List<Map<String, Object>> payrollData) {
        List<AnomalyDTO> anomalies = anomalyService.detectPayrollAnomalies(payrollData);
        return ResponseEntity.ok(anomalies);
    }

    @GetMapping("/anomaly/unresolved")
    public ResponseEntity<List<AnomalyDTO>> getUnresolvedAnomalies() {
        List<AnomalyDTO> anomalies = anomalyService.getUnresolvedAnomalies();
        return ResponseEntity.ok(anomalies);
    }

    @GetMapping("/anomaly/employee/{employeeId}")
    public ResponseEntity<List<AnomalyDTO>> getEmployeeAnomalies(@PathVariable Integer employeeId) {
        List<AnomalyDTO> anomalies = anomalyService.getEmployeeAnomalies(employeeId);
        return ResponseEntity.ok(anomalies);
    }

    @PutMapping("/anomaly/{anomalyId}/resolve")
    public ResponseEntity<Void> resolveAnomaly(@PathVariable Long anomalyId) {
        anomalyService.resolveAnomaly(anomalyId);
        return ResponseEntity.ok().build();
    }
}

