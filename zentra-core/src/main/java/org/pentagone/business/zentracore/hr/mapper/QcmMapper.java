package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.pentagone.business.zentracore.hr.dto.QcmDto;
import org.pentagone.business.zentracore.hr.entity.Qcm;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface QcmMapper {
    Qcm toEntity(QcmDto dto);
    QcmDto toDto(Qcm entity);

    @AfterMapping
    default void linkQuestions(@MappingTarget Qcm qcm) {
        if (qcm.getQuestions() != null) qcm.getQuestions().forEach(q -> q.setQcm(qcm));
    }
}
