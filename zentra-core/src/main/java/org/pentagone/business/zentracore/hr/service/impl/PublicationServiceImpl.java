package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.hr.dto.PublicationDto;
import org.pentagone.business.zentracore.hr.entity.Job;
import org.pentagone.business.zentracore.hr.entity.Publication;
import org.pentagone.business.zentracore.hr.mapper.PublicationMapper;
import org.pentagone.business.zentracore.hr.repository.JobRepository;
import org.pentagone.business.zentracore.hr.repository.PublicationRepository;
import org.pentagone.business.zentracore.hr.service.PublicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final JobRepository jobRepository;
    private final PublicationMapper publicationMapper;

    public PublicationServiceImpl(PublicationRepository publicationRepository,
                                  JobRepository jobRepository,
                                  PublicationMapper publicationMapper) {
        this.publicationRepository = publicationRepository;
        this.jobRepository = jobRepository;
        this.publicationMapper = publicationMapper;
    }

    private void validate(Publication p) {
        if (p.getTitle() == null || p.getTitle().isBlank())
            throw new IllegalArgumentException("Le titre de la publication est obligatoire");
        if (p.getDescription() == null || p.getDescription().isBlank())
            throw new IllegalArgumentException("La description de la publication est obligatoire");
        if (p.getNumberOfPositions() == null || p.getNumberOfPositions() <= 0)
            throw new IllegalArgumentException("Le nombre de postes doit être supérieur à 0");
        if (p.getPublishedDate() == null) p.setPublishedDate(java.time.LocalDate.now());
        if (p.getJob() == null || p.getJob().getId() == null)
            throw new IllegalArgumentException("Un job valide est requis pour créer une publication");

        // Vérifier l'existence du job
        Job job = jobRepository.findById(p.getJob().getId())
                .orElseThrow(() -> new IllegalArgumentException("Le job associé n'existe pas"));
        p.setJob(job);
    }

    @Override
    public PublicationDto create(PublicationDto dto) {
        Publication p = publicationMapper.toEntity(dto);
        if (p.getId() != null) throw new IllegalArgumentException("Une nouvelle publication ne doit pas avoir d'ID");
        validate(p);
        return publicationMapper.toDto(publicationRepository.save(p));
    }

    @Override
    public PublicationDto update(PublicationDto dto) {
        Publication p = publicationMapper.toEntity(dto);
        if (p.getId() == null || !publicationRepository.existsById(p.getId()))
            throw new EntityNotFoundException("Publication introuvable");
        validate(p);
        return publicationMapper.toDto(publicationRepository.save(p));
    }

    @Override
    public PublicationDto findById(Long id) {
        return publicationRepository.findById(id)
                .map(publicationMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Publication introuvable"));
    }

    @Override
    public List<PublicationDto> findAll() {
        return publicationRepository.findAll().stream().map(publicationMapper::toDto).toList();
    }

    @Override
    public void deleteById(Long id) {
        Publication p = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication introuvable"));
        publicationRepository.delete(p);
    }
}
