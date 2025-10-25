package org.pentagone.business.zentracore.hr.service.impl;

import jakarta.transaction.Transactional;
import org.pentagone.business.zentracore.common.exception.UnauthorisedAccessException;
import org.pentagone.business.zentracore.hr.dto.CandidateDto;
import org.pentagone.business.zentracore.hr.dto.CandidateMinInfoDto;
import org.pentagone.business.zentracore.hr.entity.Token;
import org.pentagone.business.zentracore.hr.mapper.CandidateMapper;
import org.pentagone.business.zentracore.hr.repository.CandidateRepository;
import org.pentagone.business.zentracore.hr.repository.TokenRepository;
import org.pentagone.business.zentracore.hr.service.CandidateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final TokenRepository tokenRepository;
    private final CandidateMapper candidateMapper;

    public CandidateServiceImpl(CandidateRepository candidateRepository, TokenRepository tokenRepository, CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.tokenRepository = tokenRepository;
        this.candidateMapper = candidateMapper;
    }


    @Override
    public CandidateDto createCandidate(CandidateDto candidateDto) {
        return null;
    }

    @Override
    public CandidateDto getCandidateById(Long id) {
        return null;
    }

    @Override
    public List<CandidateDto> getCandidates() {
        return List.of();
    }

    @Override
    public CandidateDto updateCandidate(CandidateDto candidateDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public CandidateMinInfoDto getCandidateByTokenValue(String tokenValue) {
        Token token = tokenRepository.findByValue(tokenValue).orElseThrow(() ->
                new UnauthorisedAccessException("Token is not valid"));
        if (!token.isValid())
            throw new UnauthorisedAccessException("Token is not valid");
        return candidateMapper.toMinInfoDto(token.getApplication().getCandidate());
    }
}
