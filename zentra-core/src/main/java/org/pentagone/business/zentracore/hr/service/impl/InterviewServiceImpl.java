package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.hr.dto.InterviewDto;
import org.pentagone.business.zentracore.hr.entity.Application;
import org.pentagone.business.zentracore.hr.entity.Candidate;
import org.pentagone.business.zentracore.hr.entity.Employee;
import org.pentagone.business.zentracore.hr.entity.Interview;
import org.pentagone.business.zentracore.hr.mapper.InterviewMapper;
import org.pentagone.business.zentracore.hr.repository.ApplicationRepository;
import org.pentagone.business.zentracore.hr.repository.CandidateRepository;
import org.pentagone.business.zentracore.hr.repository.EmployeeRepository;
import org.pentagone.business.zentracore.hr.repository.InterviewRepository;
import org.pentagone.business.zentracore.hr.service.InterviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewMapper interviewMapper;
    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;
    private final ApplicationRepository applicationRepository;

    public InterviewServiceImpl(InterviewRepository interviewRepository,
                                InterviewMapper interviewMapper,
                                CandidateRepository candidateRepository,
                                EmployeeRepository employeeRepository,
                                ApplicationRepository applicationRepository) {
        this.interviewRepository = interviewRepository;
        this.interviewMapper = interviewMapper;
        this.candidateRepository = candidateRepository;
        this.employeeRepository = employeeRepository;
        this.applicationRepository = applicationRepository;
    }

    private void validateInterview(InterviewDto interviewDto) {
        if (interviewDto.getCandidateId() == null) {
            throw new IllegalArgumentException("Le candidat est obligatoire");
        }
        if (interviewDto.getInterviewerId() == null) {
            throw new IllegalArgumentException("Le recruteur est obligatoire");
        }
        if (interviewDto.getInterviewDate() == null) {
            throw new IllegalArgumentException("La date de l'entretien est obligatoire");
        }
        if (interviewDto.getStartTime() == null) {
            throw new IllegalArgumentException("L'heure de début est obligatoire");
        }
        if (interviewDto.getDurationMinutes() == null || interviewDto.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("La durée doit être supérieure à 0");
        }
        if (interviewDto.getInterviewType() == null || interviewDto.getInterviewType().isEmpty()) {
            throw new IllegalArgumentException("Le type d'entretien est obligatoire");
        }

        // Validation du type
        String type = interviewDto.getInterviewType().toUpperCase();
        if (!type.equals("PRESENTIEL") && !type.equals("VISIO") && !type.equals("TELEPHONIQUE")) {
            throw new IllegalArgumentException("Type d'entretien invalide. Valeurs acceptées: PRESENTIEL, VISIO, TELEPHONIQUE");
        }
    }

    @Override
    public InterviewDto createInterview(InterviewDto interviewDto) {
        if (interviewDto.getId() != null) {
            throw new IllegalArgumentException("Un nouvel entretien ne peut pas avoir d'ID");
        }

        validateInterview(interviewDto);

        Interview interview = interviewMapper.toEntity(interviewDto);

        // Vérifier que le candidat existe
        Candidate candidate = candidateRepository.findById(interviewDto.getCandidateId())
                .orElseThrow(() -> new EntityNotFoundException("Candidat non trouvé avec l'ID: " + interviewDto.getCandidateId()));
        interview.setCandidate(candidate);

        // Vérifier que le recruteur existe
        Employee interviewer = employeeRepository.findById(interviewDto.getInterviewerId())
                .orElseThrow(() -> new EntityNotFoundException("Recruteur non trouvé avec l'ID: " + interviewDto.getInterviewerId()));
        interview.setInterviewer(interviewer);

        // Associer l'application si fournie - charger l'entité gérée depuis la BD
        if (interviewDto.getApplicationId() != null) {
            Application application = applicationRepository.findById(interviewDto.getApplicationId())
                    .orElseThrow(() -> new EntityNotFoundException("Candidature non trouvée avec l'ID: " + interviewDto.getApplicationId()));
            interview.setApplication(application);
        } else {
            interview.setApplication(null);
        }

        // Définir le statut par défaut
        if (interview.getStatus() == null || interview.getStatus().isEmpty()) {
            interview.setStatus("PLANIFIE");
        }

        Interview saved = interviewRepository.save(interview);
        return interviewMapper.toDto(saved);
    }

    @Override
    public InterviewDto updateInterview(InterviewDto interviewDto) {
        if (interviewDto.getId() == null) {
            throw new IllegalArgumentException("L'ID de l'entretien est obligatoire pour la mise à jour");
        }

        Interview existingInterview = interviewRepository.findById(interviewDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Entretien non trouvé avec l'ID: " + interviewDto.getId()));

        validateInterview(interviewDto);

        // Vérifier que le candidat existe
        Candidate candidate = candidateRepository.findById(interviewDto.getCandidateId())
                .orElseThrow(() -> new EntityNotFoundException("Candidat non trouvé avec l'ID: " + interviewDto.getCandidateId()));
        existingInterview.setCandidate(candidate);

        // Vérifier que le recruteur existe
        Employee interviewer = employeeRepository.findById(interviewDto.getInterviewerId())
                .orElseThrow(() -> new EntityNotFoundException("Recruteur non trouvé avec l'ID: " + interviewDto.getInterviewerId()));
        existingInterview.setInterviewer(interviewer);

        // Associer l'application si fournie - charger l'entité gérée depuis la BD
        if (interviewDto.getApplicationId() != null) {
            Application application = applicationRepository.findById(interviewDto.getApplicationId())
                    .orElseThrow(() -> new EntityNotFoundException("Candidature non trouvée avec l'ID: " + interviewDto.getApplicationId()));
            existingInterview.setApplication(application);
        } else {
            existingInterview.setApplication(null);
        }

        // Mettre à jour les autres champs
        existingInterview.setInterviewDate(interviewDto.getInterviewDate());
        existingInterview.setStartTime(interviewDto.getStartTime());
        existingInterview.setDurationMinutes(interviewDto.getDurationMinutes());
        existingInterview.setInterviewType(interviewDto.getInterviewType().toUpperCase());
        existingInterview.setLocation(interviewDto.getLocation());
        existingInterview.setStatus(interviewDto.getStatus() != null ? interviewDto.getStatus().toUpperCase() : "PLANIFIE");
        existingInterview.setComment(interviewDto.getComment());
        existingInterview.setScore(interviewDto.getScore());

        Interview updated = interviewRepository.save(existingInterview);
        return interviewMapper.toDto(updated);
    }

    @Override
    public InterviewDto getInterviewById(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entretien non trouvé avec l'ID: " + id));
        return interviewMapper.toDto(interview);
    }

    @Override
    public List<InterviewDto> getAllInterviews() {
        return interviewRepository.findAll().stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entretien non trouvé avec l'ID: " + id));
        interviewRepository.delete(interview);
    }

    @Override
    public List<InterviewDto> getInterviewsByCandidate(Long candidateId) {
        return interviewRepository.findByCandidateId(candidateId).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDto> getInterviewsByInterviewer(Long interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDto> getInterviewsByType(String interviewType) {
        return interviewRepository.findByInterviewType(interviewType.toUpperCase()).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDto> getInterviewsByStatus(String status) {
        return interviewRepository.findByStatus(status.toUpperCase()).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDto> getInterviewsByDate(LocalDate date) {
        return interviewRepository.findByInterviewDate(date).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDto> getInterviewsByDateRange(LocalDate startDate, LocalDate endDate) {
        return interviewRepository.findByInterviewDateBetween(startDate, endDate).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDto> getInterviewsByInterviewerAndStatus(Long interviewerId, String status) {
        return interviewRepository.findByInterviewerIdAndStatus(interviewerId, status.toUpperCase()).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InterviewDto updateStatus(Long id, String status) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entretien non trouvé avec l'ID: " + id));

        String newStatus = status.toUpperCase();
        if (!newStatus.equals("PLANIFIE") && !newStatus.equals("REALISE") && !newStatus.equals("ANNULE")) {
            throw new IllegalArgumentException("Statut invalide. Valeurs acceptées: PLANIFIE, REALISE, ANNULE");
        }

        interview.setStatus(newStatus);

        // Si une application existe, s'assurer qu'elle est gérée
        if (interview.getApplication() != null && interview.getApplication().getId() != null) {
            Application application = applicationRepository.findById(interview.getApplication().getId())
                    .orElse(null);
            interview.setApplication(application);
        }

        Interview updated = interviewRepository.save(interview);
        return interviewMapper.toDto(updated);
    }

    @Override
    public InterviewDto completeInterview(Long id, Double score, String comment) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entretien non trouvé avec l'ID: " + id));

        if (score != null && (score < 0 || score > 20)) {
            throw new IllegalArgumentException("La note doit être entre 0 et 20");
        }

        interview.setScore(score);
        interview.setComment(comment);
        interview.setStatus("REALISE");

        // Si une application existe, s'assurer qu'elle est gérée
        if (interview.getApplication() != null && interview.getApplication().getId() != null) {
            Application application = applicationRepository.findById(interview.getApplication().getId())
                    .orElse(null);
            interview.setApplication(application);
        }

        Interview updated = interviewRepository.save(interview);
        return interviewMapper.toDto(updated);
    }

    @Override
    public InterviewDto rescheduleInterview(Long id, LocalDate newDate, LocalTime newTime) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entretien non trouvé avec l'ID: " + id));

        if (newDate == null) {
            throw new IllegalArgumentException("La nouvelle date est obligatoire");
        }
        if (newTime == null) {
            throw new IllegalArgumentException("La nouvelle heure est obligatoire");
        }

        interview.setInterviewDate(newDate);
        interview.setStartTime(newTime);
        interview.setStatus("PLANIFIE");

        // Si une application existe, s'assurer qu'elle est gérée
        if (interview.getApplication() != null && interview.getApplication().getId() != null) {
            Application application = applicationRepository.findById(interview.getApplication().getId())
                    .orElse(null);
            interview.setApplication(application);
        }

        Interview updated = interviewRepository.save(interview);
        return interviewMapper.toDto(updated);
    }
}

