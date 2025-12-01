package org.pentagone.business.zentracore.hr.ai.prediction.service;

        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
        import org.pentagone.business.zentracore.hr.ai.prediction.dto.AnomalyDTO;
        import org.pentagone.business.zentracore.hr.ai.prediction.entity.Anomaly;
        import org.pentagone.business.zentracore.hr.ai.prediction.repository.AnomalyRepository;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;
        import java.util.stream.Collectors;

        @Service
        @RequiredArgsConstructor
        @Slf4j
        public class AnomalyDetectionService {

            private final AnomalyRepository anomalyRepository;

            /**
             * Detect anomalies in attendance data
             */
            @Transactional
            public List<AnomalyDTO> detectAttendanceAnomalies(List<Map<String, Object>> attendanceData) {
                log.info("Detecting attendance anomalies for {} records", attendanceData.size());
                List<AnomalyDTO> anomalies = new ArrayList<>();

                for (Map<String, Object> record : attendanceData) {
                    Integer employeeId = (Integer) record.get("employeeId");
                    String employeeName = (String) record.get("employeeName");

                    // Check for unusual patterns
                    if (record.containsKey("consecutiveAbsences")) {
                        int consecutiveAbsences = (Integer) record.get("consecutiveAbsences");
                        if (consecutiveAbsences >= 3) {
                            anomalies.add(createAnomaly(
                                    "ATTENDANCE",
                                    employeeId,
                                    employeeName,
                                    "Absences consécutives inhabituelles: " + consecutiveAbsences + " jours",
                                    consecutiveAbsences >= 5 ? "HIGH" : "MEDIUM",
                                    "attendance_" + employeeId + "_" + LocalDateTime.now()
                            ));
                        }
                    }

                    // Check for late arrivals pattern
                    if (record.containsKey("lateArrivalsThisMonth")) {
                        int lateArrivals = (Integer) record.get("lateArrivalsThisMonth");
                        if (lateArrivals >= 5) {
                            anomalies.add(createAnomaly(
                                    "ATTENDANCE",
                                    employeeId,
                                    employeeName,
                                    "Retards fréquents ce mois: " + lateArrivals + " fois",
                                    "MEDIUM",
                                    "late_arrivals_" + employeeId
                            ));
                        }
                    }

                    // Check for unusual work hours
                    if (record.containsKey("avgDailyHours")) {
                        double avgHours = ((Number) record.get("avgDailyHours")).doubleValue();
                        if (avgHours < 6.0) {
                            anomalies.add(createAnomaly(
                                    "HOURS",
                                    employeeId,
                                    employeeName,
                                    "Heures de travail inhabituellement basses: " + String.format("%.1f", avgHours) + "h/jour",
                                    "HIGH",
                                    "low_hours_" + employeeId
                            ));
                        } else if (avgHours > 12.0) {
                            anomalies.add(createAnomaly(
                                    "HOURS",
                                    employeeId,
                                    employeeName,
                                    "Heures de travail excessives: " + String.format("%.1f", avgHours) + "h/jour",
                                    "MEDIUM",
                                    "high_hours_" + employeeId
                            ));
                        }
                    }
                }

                return anomalies;
            }

            /**
             * Detect anomalies in payroll data
             */
            @Transactional
            public List<AnomalyDTO> detectPayrollAnomalies(List<Map<String, Object>> payrollData) {
                log.info("Detecting payroll anomalies for {} records", payrollData.size());
                List<AnomalyDTO> anomalies = new ArrayList<>();

                // Calculate statistics for the dataset
                DescriptiveStatistics salaryStats = new DescriptiveStatistics();
                for (Map<String, Object> record : payrollData) {
                    if (record.containsKey("salary")) {
                        salaryStats.addValue(((Number) record.get("salary")).doubleValue());
                    }
                }

                double meanSalary = salaryStats.getMean();
                double stdDevSalary = salaryStats.getStandardDeviation();

                for (Map<String, Object> record : payrollData) {
                    Integer employeeId = (Integer) record.get("employeeId");
                    String employeeName = (String) record.get("employeeName");

                    // Detect sudden salary changes
                    if (record.containsKey("salaryChange")) {
                        double salaryChange = ((Number) record.get("salaryChange")).doubleValue();
                        if (Math.abs(salaryChange) > 0.3) { // More than 30% change
                            anomalies.add(createAnomaly(
                                    "PAYROLL",
                                    employeeId,
                                    employeeName,
                                    "Changement de salaire important: " + String.format("%.1f", salaryChange * 100) + "%",
                                    Math.abs(salaryChange) > 0.5 ? "HIGH" : "MEDIUM",
                                    "salary_change_" + employeeId
                            ));
                        }
                    }

                    // Detect outliers (Z-score method)
                    if (record.containsKey("salary")) {
                        double salary = ((Number) record.get("salary")).doubleValue();
                        double zScore = (salary - meanSalary) / stdDevSalary;
                        if (Math.abs(zScore) > 3) {
                            anomalies.add(createAnomaly(
                                    "PAYROLL",
                                    employeeId,
                                    employeeName,
                                    "Salaire statistiquement inhabituel: " + salary + " € (Z-score: " +
                                            String.format("%.2f", zScore) + ")",
                                    "MEDIUM",
                                    "salary_outlier_" + employeeId
                            ));
                        }
                    }

                    // Check for missing deductions
                    if (record.containsKey("deductionsApplied")) {
                        boolean deductionsApplied = (Boolean) record.get("deductionsApplied");
                        if (!deductionsApplied) {
                            anomalies.add(createAnomaly(
                                    "PAYROLL",
                                    employeeId,
                                    employeeName,
                                    "Déductions sociales non appliquées",
                                    "CRITICAL",
                                    "missing_deductions_" + employeeId
                            ));
                        }
                    }

                    // Check overtime calculation
                    if (record.containsKey("overtimeHours") && record.containsKey("overtimePay")) {
                        double overtimeHours = ((Number) record.get("overtimeHours")).doubleValue();
                        double overtimePay = ((Number) record.get("overtimePay")).doubleValue();
                        double expectedPay = overtimeHours * 15; // Example rate
                        if (overtimeHours > 0 && Math.abs(overtimePay - expectedPay) > expectedPay * 0.2) {
                            anomalies.add(createAnomaly(
                                    "PAYROLL",
                                    employeeId,
                                    employeeName,
                                    "Calcul des heures supplémentaires potentiellement incorrect",
                                    "MEDIUM",
                                    "overtime_calc_" + employeeId
                            ));
                        }
                    }
                }

                return anomalies;
            }

            /**
             * Detect anomalies using statistical analysis
             */
            public List<AnomalyDTO> detectStatisticalAnomalies(List<Double> values, List<Map<String, Object>> metadata,
                                                                String anomalyType) {
                List<AnomalyDTO> anomalies = new ArrayList<>();

                if (values.isEmpty()) {
                    return anomalies;
                }

                DescriptiveStatistics stats = new DescriptiveStatistics();
                values.forEach(stats::addValue);

                double mean = stats.getMean();
                double stdDev = stats.getStandardDeviation();
                double median = stats.getPercentile(50);

                // IQR method for outlier detection
                double q1 = stats.getPercentile(25);
                double q3 = stats.getPercentile(75);
                double iqr = q3 - q1;
                double lowerBound = q1 - 1.5 * iqr;
                double upperBound = q3 + 1.5 * iqr;

                for (int i = 0; i < values.size(); i++) {
                    double value = values.get(i);
                    Map<String, Object> meta = metadata.get(i);

                    if (value < lowerBound || value > upperBound) {
                        Integer employeeId = (Integer) meta.get("employeeId");
                        String employeeName = (String) meta.get("employeeName");

                        anomalies.add(createAnomaly(
                                anomalyType,
                                employeeId,
                                employeeName,
                                "Valeur anormale détectée: " + String.format("%.2f", value) +
                                        " (médiane: " + String.format("%.2f", median) + ")",
                                value < lowerBound * 0.5 || value > upperBound * 1.5 ? "HIGH" : "MEDIUM",
                                anomalyType.toLowerCase() + "_statistical_" + employeeId
                        ));
                    }
                }

                return anomalies;
            }

            private AnomalyDTO createAnomaly(String type, Integer employeeId, String employeeName,
                                             String description, String severity, String reference) {
                Anomaly anomaly = new Anomaly();
                anomaly.setAnomalyType(type);
                anomaly.setEmployeeId(employeeId);
                anomaly.setEmployeeName(employeeName);
                anomaly.setDescription(description);
                anomaly.setSeverity(severity);
                anomaly.setDataReference(reference);
                anomaly.setResolved(false);
                anomaly.setDetectedAt(LocalDateTime.now());

                Anomaly saved = anomalyRepository.save(anomaly);
                return convertToDTO(saved);
            }

            public List<AnomalyDTO> getUnresolvedAnomalies() {
                return anomalyRepository.findByResolvedFalseOrderByDetectedAtDesc()
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }

            public List<AnomalyDTO> getEmployeeAnomalies(Integer employeeId) {
                return anomalyRepository.findByEmployeeId(employeeId)
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }

            @Transactional
            public void resolveAnomaly(Long anomalyId) {
                anomalyRepository.findById(anomalyId).ifPresent(anomaly -> {
                    anomaly.setResolved(true);
                    anomalyRepository.save(anomaly);
                });
            }

            private AnomalyDTO convertToDTO(Anomaly anomaly) {
                AnomalyDTO dto = new AnomalyDTO();
                dto.setId(anomaly.getId());
                dto.setAnomalyType(anomaly.getAnomalyType());
                dto.setEmployeeId(anomaly.getEmployeeId());
                dto.setEmployeeName(anomaly.getEmployeeName());
                dto.setDescription(anomaly.getDescription());
                dto.setSeverity(anomaly.getSeverity());
                dto.setDetectedAt(anomaly.getDetectedAt());
                dto.setResolved(anomaly.getResolved());
                dto.setDataReference(anomaly.getDataReference());
                return dto;
            }
        }