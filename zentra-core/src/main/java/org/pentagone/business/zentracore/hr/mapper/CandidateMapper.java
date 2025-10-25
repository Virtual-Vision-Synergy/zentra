package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pentagone.business.zentracore.hr.dto.CandidateMinInfoDto;
import org.pentagone.business.zentracore.hr.entity.Candidate;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    @Mapping(target = "applicationId", source = "application.id")
    CandidateMinInfoDto toMinInfoDto(Candidate candidate);
}
