package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.hr.dto.QcmDto;
import org.pentagone.business.zentracore.hr.entity.Choice;
import org.pentagone.business.zentracore.hr.entity.Qcm;
import org.pentagone.business.zentracore.hr.mapper.QcmMapper;
import org.pentagone.business.zentracore.hr.repository.QcmRepository;
import org.pentagone.business.zentracore.hr.service.QcmService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QcmServiceImpl implements QcmService {
    private final QcmRepository qcmRepository;
    private final QcmMapper qcmMapper;

    public QcmServiceImpl(QcmRepository qcmRepository, QcmMapper qcmMapper) {
        this.qcmRepository = qcmRepository;
        this.qcmMapper = qcmMapper;
    }

    private void verifyQcm(Qcm qcm) {
        if (qcm.getTitle() == null || qcm.getTitle().isEmpty())
            throw new IllegalArgumentException("Title is empty");
        if (qcm.getDescription() == null || qcm.getDescription().isEmpty())
            throw new IllegalArgumentException("Description is empty");
        if (qcm.getRequiredScore() == null || qcm.getRequiredScore() < 0)
            throw new IllegalArgumentException("Required score is empty");
        if (qcm.getQuestions() == null || qcm.getQuestions().isEmpty())
            throw new IllegalArgumentException("Questions is empty");
        qcm.getQuestions().forEach(q -> {
            if (q.getLibelle() == null || q.getLibelle().isEmpty())
                throw new IllegalArgumentException("Libelle is empty");
            if (q.getChoices() == null || q.getChoices().isEmpty())
                throw new IllegalArgumentException("Choices is empty");
            if (q.getChoices().stream().noneMatch(Choice::isCorrect))
                throw new IllegalArgumentException("No correct choice");
            q.getChoices().forEach(c -> {
                if (c.getLibelle() == null || c.getLibelle().isEmpty())
                    throw new IllegalArgumentException("Libelle is empty");
            });
        });
    }

    @Override
    public QcmDto createQcm(QcmDto qcmDto) {
        Qcm qcm = qcmMapper.toEntity(qcmDto);
        verifyQcm(qcm);
        return qcmMapper.toDto(qcmRepository.save(qcm));
    }

    @Override
    public QcmDto updateQcm(QcmDto qcmDto) {
        Qcm qcm = qcmMapper.toEntity(qcmDto);
        verifyQcm(qcm);
        return qcmMapper.toDto(qcmRepository.save(qcm));
    }

    @Override
    public QcmDto getQcmById(Long id) {
        return qcmMapper.toDto(qcmRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Qcm not found")));
    }

    @Override
    public List<QcmDto> getQcms() {
        return qcmRepository.findAll().stream().map(qcmMapper::toDto).toList();
    }

    @Override
    public void deleteById(Long id) {
        Qcm qcm = qcmRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Qcm not found"));
        qcmRepository.delete(qcm);
    }
}
