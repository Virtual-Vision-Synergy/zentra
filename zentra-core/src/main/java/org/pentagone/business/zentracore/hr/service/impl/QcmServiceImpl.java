package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.hr.dto.QcmDto;
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

    }

    @Override
    public QcmDto createQcm(QcmDto qcmDto) {
        return null;
    }

    @Override
    public QcmDto updateQcm(QcmDto qcmDto) {
        return null;
    }

    @Override
    public QcmDto getQcmById(Long id) {
        return null;
    }

    @Override
    public List<QcmDto> getQcms() {
        return List.of();
    }
}
