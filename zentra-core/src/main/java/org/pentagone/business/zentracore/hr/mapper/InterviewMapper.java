package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.pentagone.business.zentracore.hr.dto.InterviewDto;
import org.pentagone.business.zentracore.hr.entity.Candidate;
import org.pentagone.business.zentracore.hr.entity.Employee;
import org.pentagone.business.zentracore.hr.entity.Interview;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

    @Mapping(target = "candidateId", source = "candidate.id")
    @Mapping(target = "candidateName", source = "candidate", qualifiedByName = "getCandidateName")
    @Mapping(target = "candidateEmail", source = "candidate.email")
    @Mapping(target = "interviewerId", source = "interviewer.id")
    @Mapping(target = "interviewerName", source = "interviewer", qualifiedByName = "getInterviewerName")
    @Mapping(target = "applicationId", source = "application.id")
    InterviewDto toDto(Interview entity);

    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "interviewer", ignore = true)
    @Mapping(target = "application", ignore = true)
    Interview toEntity(InterviewDto dto);

    @Named("getCandidateName")
    default String getCandidateName(Candidate candidate) {
        if (candidate == null) return null;
        return candidate.getFirstName() + " " + candidate.getLastName();
    }

    @Named("getInterviewerName")
    default String getInterviewerName(Employee employee) {
        if (employee == null) return null;
        return employee.getFirstName() + " " + employee.getLastName();
    }
}

