package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.CandidateDto;
import org.pentagone.business.zentracore.hr.dto.CandidateMinInfoDto;

import java.util.List;

public interface CandidateService {
    CandidateDto createCandidate(CandidateDto candidateDto);
    CandidateDto getCandidateById(Long id);
    List<CandidateDto> getCandidates();
    CandidateDto updateCandidate(CandidateDto candidateDto);
    void deleteById(Long id);

    CandidateMinInfoDto getCandidateByTokenValue(String tokenValue);
}
