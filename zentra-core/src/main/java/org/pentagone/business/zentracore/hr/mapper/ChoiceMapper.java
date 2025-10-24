package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.Mapper;
import org.pentagone.business.zentracore.hr.dto.ChoiceDto;
import org.pentagone.business.zentracore.hr.entity.Choice;

@Mapper(componentModel = "spring")
public interface ChoiceMapper {
    Choice toEntity(ChoiceDto dto);
    ChoiceDto toDto(Choice entity);
}
