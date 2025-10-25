package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.pentagone.business.zentracore.hr.dto.AnswerDto;
import org.pentagone.business.zentracore.hr.entity.Answer;
import org.pentagone.business.zentracore.hr.repository.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public abstract class AnswerMapper {
    @Autowired
    protected ChoiceRepository choiceRepository;

    public abstract Answer toEntity(AnswerDto dto);

    @Mapping(target = "attemptId", source = "attempt.id")
    @Mapping(target = "choiceId", source = "choice.id")
    public abstract AnswerDto toDto(Answer entity);

    @AfterMapping
    protected void mapRelations(@MappingTarget Answer answer, AnswerDto dto) {
        if (dto == null) return;
        if (dto.getChoiceId() != null) answer.setChoice(choiceRepository.findById(dto.getChoiceId()).orElse(null));
    }
}
