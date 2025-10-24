package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.QcmDto;

import java.util.List;

public interface QcmService {
    QcmDto createQcm(QcmDto qcmDto);
    QcmDto updateQcm(QcmDto qcmDto);
    QcmDto getQcmById(Long id);
    List<QcmDto> getQcms();
}
