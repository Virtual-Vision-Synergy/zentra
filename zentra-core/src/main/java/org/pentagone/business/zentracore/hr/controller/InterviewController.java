package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.InterviewDto;
import org.pentagone.business.zentracore.hr.service.InterviewService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<InterviewDto> createInterview(@RequestBody InterviewDto interviewDto) {
        InterviewDto created = interviewService.createInterview(interviewDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<InterviewDto> updateInterview(@RequestBody InterviewDto interviewDto) {
        InterviewDto updated = interviewService.updateInterview(interviewDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewDto> getInterviewById(@PathVariable Long id) {
        InterviewDto interview = interviewService.getInterviewById(id);
        return new ResponseEntity<>(interview, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<InterviewDto>> getAllInterviews() {
        List<InterviewDto> interviews = interviewService.getAllInterviews();
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterviewById(@PathVariable Long id) {
        interviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints de recherche spécifiques

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<InterviewDto>> getInterviewsByCandidate(@PathVariable Long candidateId) {
        List<InterviewDto> interviews = interviewService.getInterviewsByCandidate(candidateId);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @GetMapping("/interviewer/{interviewerId}")
    public ResponseEntity<List<InterviewDto>> getInterviewsByInterviewer(@PathVariable Long interviewerId) {
        List<InterviewDto> interviews = interviewService.getInterviewsByInterviewer(interviewerId);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @GetMapping("/type/{interviewType}")
    public ResponseEntity<List<InterviewDto>> getInterviewsByType(@PathVariable String interviewType) {
        List<InterviewDto> interviews = interviewService.getInterviewsByType(interviewType);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InterviewDto>> getInterviewsByStatus(@PathVariable String status) {
        List<InterviewDto> interviews = interviewService.getInterviewsByStatus(status);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<InterviewDto>> getInterviewsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<InterviewDto> interviews = interviewService.getInterviewsByDate(date);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InterviewDto>> getInterviewsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<InterviewDto> interviews = interviewService.getInterviewsByDateRange(startDate, endDate);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @GetMapping("/interviewer/{interviewerId}/status/{status}")
    public ResponseEntity<List<InterviewDto>> getInterviewsByInterviewerAndStatus(
            @PathVariable Long interviewerId,
            @PathVariable String status) {
        List<InterviewDto> interviews = interviewService.getInterviewsByInterviewerAndStatus(interviewerId, status);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    // Endpoints de gestion du statut

    @PatchMapping("/{id}/status")
    public ResponseEntity<InterviewDto> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Le statut est obligatoire");
        }
        InterviewDto updated = interviewService.updateStatus(id, status);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<InterviewDto> completeInterview(
            @PathVariable Long id,
            @RequestBody Map<String, Object> completion) {
        Double score = completion.get("score") != null ?
                Double.valueOf(completion.get("score").toString()) : null;
        String comment = completion.get("comment") != null ?
                completion.get("comment").toString() : null;

        InterviewDto updated = interviewService.completeInterview(id, score, comment);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<InterviewDto> rescheduleInterview(
            @PathVariable Long id,
            @RequestBody Map<String, String> reschedule) {
        LocalDate newDate = LocalDate.parse(reschedule.get("date"));
        LocalTime newTime = LocalTime.parse(reschedule.get("time"));

        InterviewDto updated = interviewService.rescheduleInterview(id, newDate, newTime);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}

