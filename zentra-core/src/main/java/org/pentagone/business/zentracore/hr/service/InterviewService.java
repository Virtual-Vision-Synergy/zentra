package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.InterviewDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface InterviewService {
    InterviewDto createInterview(InterviewDto interviewDto);
    InterviewDto updateInterview(InterviewDto interviewDto);
    InterviewDto getInterviewById(Long id);
    List<InterviewDto> getAllInterviews();
    void deleteById(Long id);

    // Méthodes de recherche spécifiques
    List<InterviewDto> getInterviewsByCandidate(Long candidateId);
    List<InterviewDto> getInterviewsByInterviewer(Long interviewerId);
    List<InterviewDto> getInterviewsByType(String interviewType);
    List<InterviewDto> getInterviewsByStatus(String status);
    List<InterviewDto> getInterviewsByDate(LocalDate date);
    List<InterviewDto> getInterviewsByDateRange(LocalDate startDate, LocalDate endDate);
    List<InterviewDto> getInterviewsByInterviewerAndStatus(Long interviewerId, String status);

    // Méthodes de gestion du statut
    InterviewDto updateStatus(Long id, String status);
    InterviewDto completeInterview(Long id, Double score, String comment);
    InterviewDto rescheduleInterview(Long id, LocalDate newDate, LocalTime newTime);
}

