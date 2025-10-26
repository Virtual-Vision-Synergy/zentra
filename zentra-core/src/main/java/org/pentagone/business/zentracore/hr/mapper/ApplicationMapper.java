package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pentagone.business.zentracore.hr.dto.ApplicationDto;
import org.pentagone.business.zentracore.hr.entity.Application;
import org.pentagone.business.zentracore.hr.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring", uses = {CandidateMapper.class})
public abstract class ApplicationMapper {
    @Autowired
    private PublicationRepository publicationRepository;

    public abstract Application toEntity(ApplicationDto dto);

    @Mapping(target = "publicationId", source = "publication.id")
    public abstract ApplicationDto toDto(Application entity);

    @AfterMapping
    protected void mapPublication(@MappingTarget Application entity, ApplicationDto dto) {
        if (dto.getPublicationId() != null)
            entity.setPublication(publicationRepository.findById(dto.getPublicationId()).orElse(null));
    }
}
