package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.hr.dto.AttemptDto;
import org.pentagone.business.zentracore.hr.entity.Attempt;
import org.pentagone.business.zentracore.hr.entity.Question;
import org.pentagone.business.zentracore.hr.mapper.AttemptMapper;
import org.pentagone.business.zentracore.hr.repository.AttemptRepository;
import org.pentagone.business.zentracore.hr.service.AttemptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttemptServiceImpl implements AttemptService {
    private final AttemptRepository attemptRepository;
    private final AttemptMapper attemptMapper;

    public AttemptServiceImpl(AttemptRepository attemptRepository, AttemptMapper attemptMapper) {
        this.attemptRepository = attemptRepository;
        this.attemptMapper = attemptMapper;
    }

    private void verifyAttempt(Attempt attempt) {
        if (attempt.getApplication() == null)
            throw new IllegalArgumentException("Attempt must be associated with an application.");
        if (attempt.getAnswers() == null || attempt.getAnswers().isEmpty())
            throw new IllegalArgumentException("Attempt must have at least one answer.");
        attempt.getAnswers().forEach(a -> {
            if (a.getChoice() == null)
                throw new IllegalArgumentException("Each answer must be associated with a choice.");
        });
        Set<Question> answeredQuestions = attempt.getAnswers().stream()
                .map(a -> a.getChoice().getQuestion())
                .collect(Collectors.toSet());
        Set<Question> requiredQuestions = attempt.getApplication().getQcm().getQuestions().stream()
                .filter(Question::isRequired)
                .collect(Collectors.toSet());
        if (!answeredQuestions.containsAll(requiredQuestions))
            throw new IllegalArgumentException("Every required question must be answered.");
    }

    @Override
    public AttemptDto createAttempt(AttemptDto attemptDto) {
        Attempt attempt = attemptMapper.toEntity(attemptDto);
        verifyAttempt(attempt);
        attempt.setObtainedScore(attempt.getAnswers().stream()
                .filter(a -> a.getChoice().isCorrect())
                .mapToDouble(a -> a.getChoice().getQuestion().getChoicesScore())
                .sum());
        return attemptMapper.toDto(attemptRepository.save(attempt));
    }

    @Override
    public AttemptDto getAttemptByCandidateId(Long candidateId) {
        return attemptMapper.toDto(attemptRepository.findByApplicationCandidateId(candidateId).orElseThrow(() ->
                new EntityNotFoundException("Attempt not found for candidate ID: " + candidateId)));
    }
}
