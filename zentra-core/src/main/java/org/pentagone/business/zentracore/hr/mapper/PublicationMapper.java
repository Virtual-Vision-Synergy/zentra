package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pentagone.business.zentracore.hr.dto.PublicationDto;
import org.pentagone.business.zentracore.hr.entity.Publication;

@Mapper(componentModel = "spring")
public interface PublicationMapper {
    @Mapping(target = "jobId", source = "job.id")
    PublicationDto toDto(Publication entity);

    @Mapping(target = "job.id", source = "jobId")
    Publication toEntity(PublicationDto dto);
}

