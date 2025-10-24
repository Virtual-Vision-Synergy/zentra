package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.pentagone.business.zentracore.hr.dto.QuestionDto;
import org.pentagone.business.zentracore.hr.entity.Question;

@Mapper(componentModel = "spring", uses = { ChoiceMapper.class })
public interface QuestionMapper {
    Question toEntity(QuestionDto dto);
    QuestionDto toDto(Question entity);

    @AfterMapping
    default void linkChoices(@MappingTarget Question question) {
        if (question.getChoices() != null) question.getChoices().forEach(choice -> choice.setQuestion(question));
    }
}


