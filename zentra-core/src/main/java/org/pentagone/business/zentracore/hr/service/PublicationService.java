package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.PublicationDto;

import java.util.List;

public interface PublicationService {
    PublicationDto create(PublicationDto dto);
    PublicationDto update(PublicationDto dto);
    PublicationDto findById(Long id);
    List<PublicationDto> findAll();
    void deleteById(Long id);
}

