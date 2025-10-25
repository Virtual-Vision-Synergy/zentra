package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.AttemptDto;

public interface AttemptService {
    AttemptDto createAttempt(AttemptDto attemptDto);
    AttemptDto getAttemptByCandidateId(Long candidateId);
}
