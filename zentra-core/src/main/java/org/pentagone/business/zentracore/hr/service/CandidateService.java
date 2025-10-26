package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.CandidateDto;
import org.pentagone.business.zentracore.hr.dto.CandidateMinInfoDto;
import org.pentagone.business.zentracore.hr.entity.Candidate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface CandidateService {
    CandidateDto createCandidate(CandidateDto candidateDto);
    CandidateDto updateCandidate(CandidateDto candidateDto);
    CandidateDto getCandidateById(Long id);
    List<CandidateDto> getAllCandidates();
    void deleteById(Long id);
    void verifyCandidate(Candidate candidate);
    CandidateMinInfoDto getCandidateByTokenValue(String tokenValue);
}

