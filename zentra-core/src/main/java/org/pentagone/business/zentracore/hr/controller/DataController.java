package org.pentagone.business.zentracore.hr.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller pour fournir des données de base aux composants IA du frontend
 */
@Slf4j
@RestController
@RequestMapping("/hr")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DataController {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Récupère la liste de tous les employés
     */
    @GetMapping("/employees")
    public ResponseEntity<List<Map<String, Object>>> getAllEmployees() {
        try {
            String sql = "SELECT e.id, " +
                        "e.first_name as \"firstName\", " +
                        "e.last_name as \"lastName\", " +
                        "j.title as \"position\", " +
                        "e.base_salary as \"salary\" " +
                        "FROM employee e " +
                        "LEFT JOIN job j ON e.job_id = j.id " +
                        "ORDER BY e.last_name, e.first_name";
            List<Map<String, Object>> employees = jdbcTemplate.queryForList(sql);
            log.info("✅ Fetched {} employees from database", employees.size());

            // Log les premiers employés pour debug
            if (!employees.isEmpty()) {
                log.info("📋 Premier employé: {}", employees.get(0));
                log.info("📋 Clés disponibles: {}", employees.get(0).keySet());
            }

            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("❌ Error fetching employees", e);
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }

    /**
     * Récupère un employé par ID
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable Integer id) {
        try {
            String sql = "SELECT e.id, " +
                        "e.first_name as \"firstName\", " +
                        "e.last_name as \"lastName\", " +
                        "j.title as \"position\", " +
                        "e.base_salary as \"salary\" " +
                        "FROM employee e " +
                        "LEFT JOIN job j ON e.job_id = j.id " +
                        "WHERE e.id = ?";
            Map<String, Object> employee = jdbcTemplate.queryForMap(sql, id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            log.error("Error fetching employee {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}

