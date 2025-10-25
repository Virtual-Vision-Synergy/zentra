package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pentagone.business.zentracore.hr.dto.AttemptDto;
import org.pentagone.business.zentracore.hr.entity.Attempt;
import org.pentagone.business.zentracore.hr.repository.ApplicationRepository;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring", uses = { AnswerMapper.class })
public abstract class AttemptMapper {
    private final ApplicationRepository applicationRepository;

    public AttemptMapper(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public abstract Attempt toEntity(AttemptDto dto);

    @Mapping(target = "applicationId", source = "application.id")
    public abstract AttemptDto toDto(Attempt entity);

    @AfterMapping
    protected void mapRelationsAndLinkAnswers(@MappingTarget Attempt attempt, AttemptDto dto) {
        if (dto == null) return;
        if (dto.getApplicationId() != null)
            attempt.setApplication(applicationRepository.findById(dto.getApplicationId()).orElse(null));
        if (attempt.getAnswers() != null) attempt.getAnswers().forEach(a -> a.setAttempt(attempt));
    }
}
