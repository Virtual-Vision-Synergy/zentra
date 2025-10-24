package org.pentagone.business.zentracore.candidature.service;

import lombok.RequiredArgsConstructor;
import org.pentagone.business.zentracore.candidature.dto.CandidatureCreateDto;
import org.pentagone.business.zentracore.candidature.dto.CandidatureResponseDto;
import org.pentagone.business.zentracore.candidature.dto.CandidatureUpdateDto;
import org.pentagone.business.zentracore.candidature.entity.AnnonceEmploi;
import org.pentagone.business.zentracore.candidature.entity.Candidat;
import org.pentagone.business.zentracore.candidature.entity.Candidature;
import org.pentagone.business.zentracore.candidature.repository.AnnonceEmploiRepository;
import org.pentagone.business.zentracore.candidature.repository.CandidatRepository;
import org.pentagone.business.zentracore.candidature.repository.CandidatureRepository;
import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final CandidatRepository candidatRepository;
    private final AnnonceEmploiRepository annonceEmploiRepository;

    @Transactional
    public CandidatureResponseDto createCandidature(CandidatureCreateDto createDto) {
        // Verify candidat exists
        Candidat candidat = candidatRepository.findById(createDto.getIdCandidat())
            .orElseThrow(() -> new EntityNotFoundException("Candidat non trouvé avec l'ID: " + createDto.getIdCandidat()));

        // Verify annonce exists
        AnnonceEmploi annonce = annonceEmploiRepository.findById(createDto.getIdAnnonce())
            .orElseThrow(() -> new EntityNotFoundException("Annonce d'emploi non trouvée avec l'ID: " + createDto.getIdAnnonce()));

        // Check if candidature already exists
        if (candidatureRepository.findByCandidatIdCandidatAndAnnonceEmploiIdAnnonce(
                createDto.getIdCandidat(), createDto.getIdAnnonce()).isPresent()) {
            throw new IllegalArgumentException("Le candidat a déjà postulé à cette annonce");
        }

        // Create new candidature
        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setAnnonceEmploi(annonce);
        candidature.setScoreInitial(createDto.getScoreInitial());
        candidature.setCommentaireInitial(createDto.getCommentaireInitial());
        candidature.setStatut("Reçue");
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setDateDerniereModification(LocalDateTime.now());

        candidature = candidatureRepository.save(candidature);

        return mapToResponseDto(candidature);
    }

    @Transactional(readOnly = true)
    public CandidatureResponseDto getCandidatureById(Long id) {
        Candidature candidature = candidatureRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Candidature non trouvée avec l'ID: " + id));
        return mapToResponseDto(candidature);
    }

    @Transactional(readOnly = true)
    public List<CandidatureResponseDto> getAllCandidatures() {
        return candidatureRepository.findAll().stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CandidatureResponseDto> getCandidaturesByStatut(String statut) {
        return candidatureRepository.findByStatut(statut).stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CandidatureResponseDto> getCandidaturesByCandidat(Long idCandidat) {
        return candidatureRepository.findByCandidatIdCandidat(idCandidat).stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CandidatureResponseDto> getCandidaturesByAnnonce(Long idAnnonce) {
        return candidatureRepository.findByAnnonceEmploiIdAnnonce(idAnnonce).stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public CandidatureResponseDto updateCandidature(Long id, CandidatureUpdateDto updateDto) {
        Candidature candidature = candidatureRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Candidature non trouvée avec l'ID: " + id));

        if (updateDto.getStatut() != null) {
            candidature.setStatut(updateDto.getStatut());
        }
        if (updateDto.getScoreInitial() != null) {
            candidature.setScoreInitial(updateDto.getScoreInitial());
        }
        if (updateDto.getCommentaireInitial() != null) {
            candidature.setCommentaireInitial(updateDto.getCommentaireInitial());
        }

        candidature.setDateDerniereModification(LocalDateTime.now());
        candidature = candidatureRepository.save(candidature);

        return mapToResponseDto(candidature);
    }

    @Transactional
    public void deleteCandidature(Long id) {
        if (!candidatureRepository.existsById(id)) {
            throw new EntityNotFoundException("Candidature non trouvée avec l'ID: " + id);
        }
        candidatureRepository.deleteById(id);
    }

    private CandidatureResponseDto mapToResponseDto(Candidature candidature) {
        CandidatureResponseDto dto = new CandidatureResponseDto();
        dto.setIdCandidature(candidature.getIdCandidature());
        dto.setIdCandidat(candidature.getCandidat().getIdCandidat());
        dto.setNomCandidat(candidature.getCandidat().getNom());
        dto.setPrenomCandidat(candidature.getCandidat().getPrenom());
        dto.setEmailCandidat(candidature.getCandidat().getEmail());
        dto.setIdAnnonce(candidature.getAnnonceEmploi().getIdAnnonce());
        dto.setTitreAnnonce(candidature.getAnnonceEmploi().getTitreAnnonce());
        dto.setDateCandidature(candidature.getDateCandidature());
        dto.setStatut(candidature.getStatut());
        dto.setScoreInitial(candidature.getScoreInitial());
        dto.setCommentaireInitial(candidature.getCommentaireInitial());
        dto.setDateDerniereModification(candidature.getDateDerniereModification());
        return dto;
    }
}
