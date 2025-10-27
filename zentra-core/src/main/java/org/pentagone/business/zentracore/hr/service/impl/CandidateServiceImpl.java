package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.common.exception.UnauthorisedAccessException;
import org.pentagone.business.zentracore.hr.dto.CandidateDto;
import org.pentagone.business.zentracore.hr.dto.CandidateMinInfoDto;
import org.pentagone.business.zentracore.hr.entity.Candidate;
import org.pentagone.business.zentracore.hr.entity.Token;
import org.pentagone.business.zentracore.hr.mapper.CandidateMapper;
import org.pentagone.business.zentracore.hr.repository.CandidateRepository;
import org.pentagone.business.zentracore.hr.repository.TokenRepository;
import org.pentagone.business.zentracore.hr.service.CandidateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {
    private final TokenRepository tokenRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    public CandidateServiceImpl(TokenRepository tokenRepository,
                                CandidateRepository candidateRepository,
                                CandidateMapper candidateMapper) {
        this.tokenRepository = tokenRepository;
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    public void verifyCandidate(Candidate candidate) {
        if (candidate.getFirstName() == null || candidate.getFirstName().isBlank())
            throw new IllegalArgumentException("Le prénom du candidat est obligatoire");
        if (candidate.getLastName() == null || candidate.getLastName().isBlank())
            throw new IllegalArgumentException("Le nom du candidat est obligatoire");
        if (candidate.getEmail() == null || candidate.getEmail().isBlank())
            throw new IllegalArgumentException("L'email du candidat est obligatoire");
        if (candidate.getPhone() == null || candidate.getPhone().isBlank())
            throw new IllegalArgumentException("Le téléphone du candidat est obligatoire");
        if (candidate.getAddress() == null || candidate.getAddress().isBlank())
            throw new IllegalArgumentException("L'adresse du candidat est obligatoire");
        if (candidate.getCity() == null || candidate.getCity().isBlank())
            throw new IllegalArgumentException("La ville du candidat est obligatoire");
        if (candidate.getCountry() == null || candidate.getCountry().isBlank())
            throw new IllegalArgumentException("Le pays du candidat est obligatoire");
        if (candidate.getEducationLevel() == null || candidate.getEducationLevel().isBlank())
            throw new IllegalArgumentException("Le niveau d'éducation du candidat est obligatoire");
        if (candidate.getLastDegree() == null || candidate.getLastDegree().isBlank())
            throw new IllegalArgumentException("Le dernier diplôme du candidat est obligatoire");
        if (candidate.getYearsExperience() == null || candidate.getYearsExperience() < 0)
            throw new IllegalArgumentException("Les années d'expérience du candidat sont obligatoires");
        if (candidate.getSkills() == null || candidate.getSkills().isBlank())
            throw new IllegalArgumentException("Les compétences du candidat sont obligatoires");
    }

    @Override
    public CandidateDto createCandidate(CandidateDto candidateDto) {
        if (candidateDto.getId() != null)
            throw new IllegalArgumentException("Un nouveau candidat ne peut pas avoir d'ID");
        Candidate candidate = candidateMapper.toEntity(candidateDto);
        verifyCandidate(candidate);
        Candidate saved = candidateRepository.save(candidate);
        return candidateMapper.toDto(saved);
    }

    @Override
    public CandidateDto updateCandidate(CandidateDto candidateDto) {
        if (candidateDto.getId() == null)
            throw new IllegalArgumentException("L'ID du candidat est obligatoire pour la mise à jour");
        if (!candidateRepository.existsById(candidateDto.getId()))
            throw new EntityNotFoundException("Candidat non trouvé avec l'ID: " + candidateDto.getId());
        Candidate candidate = candidateMapper.toEntity(candidateDto);
        verifyCandidate(candidate);
        Candidate updated = candidateRepository.save(candidate);
        return candidateMapper.toDto(updated);
    }

    @Override
    public CandidateDto getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidat non trouvé avec l'ID: " + id));
        return candidateMapper.toDto(candidate);
    }

    @Override
    public List<CandidateDto> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(candidateMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidat non trouvé avec l'ID: " + id));
        candidateRepository.delete(candidate);
    }

    @Override
    public CandidateMinInfoDto getCandidateByTokenValue(String tokenValue) {
        Token token = tokenRepository.findByValue(tokenValue).orElseThrow(() ->
                new UnauthorisedAccessException("Invalid Token"));
        if (!token.isValid())
            throw new UnauthorisedAccessException("Invalid Token");
        return candidateMapper.toMinInfoDto(token.getApplication().getCandidate());
    }
}

