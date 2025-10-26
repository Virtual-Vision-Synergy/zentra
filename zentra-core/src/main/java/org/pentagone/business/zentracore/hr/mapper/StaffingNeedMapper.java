package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pentagone.business.zentracore.hr.dto.StaffingNeedDto;
import org.pentagone.business.zentracore.hr.entity.StaffingNeed;

@Mapper(componentModel = "spring")
public interface StaffingNeedMapper {
    
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "jobTitle", source = "job.title")
    @Mapping(target = "requestedById", source = "requestedBy.id")
    @Mapping(target = "requestedByName", expression = "java(entity.getRequestedBy() != null ? entity.getRequestedBy().getFirstName() + \" \" + entity.getRequestedBy().getLastName() : null)")
    StaffingNeedDto toDto(StaffingNeed entity);
    
    @Mapping(target = "department.id", source = "departmentId")
    @Mapping(target = "job.id", source = "jobId")
    @Mapping(target = "requestedBy.id", source = "requestedById")
    @Mapping(target = "department.name", ignore = true)
    @Mapping(target = "department.description", ignore = true)
    @Mapping(target = "department.annualBudget", ignore = true)
    @Mapping(target = "department.jobs", ignore = true)
    @Mapping(target = "department.createdAt", ignore = true)
    @Mapping(target = "department.updatedAt", ignore = true)
    @Mapping(target = "job.title", ignore = true)
    @Mapping(target = "job.description", ignore = true)
    @Mapping(target = "job.requiredDegree", ignore = true)
    @Mapping(target = "job.requiredSkills", ignore = true)
    @Mapping(target = "job.department", ignore = true)
    @Mapping(target = "job.employees", ignore = true)
    @Mapping(target = "job.publications", ignore = true)
    @Mapping(target = "job.createdAt", ignore = true)
    @Mapping(target = "job.updatedAt", ignore = true)
    @Mapping(target = "requestedBy.employeeNumber", ignore = true)
    @Mapping(target = "requestedBy.lastName", ignore = true)
    @Mapping(target = "requestedBy.firstName", ignore = true)
    @Mapping(target = "requestedBy.workEmail", ignore = true)
    @Mapping(target = "requestedBy.workPhone", ignore = true)
    @Mapping(target = "requestedBy.birthDate", ignore = true)
    @Mapping(target = "requestedBy.gender", ignore = true)
    @Mapping(target = "requestedBy.address", ignore = true)
    @Mapping(target = "requestedBy.city", ignore = true)
    @Mapping(target = "requestedBy.country", ignore = true)
    @Mapping(target = "requestedBy.hireDate", ignore = true)
    @Mapping(target = "requestedBy.baseSalary", ignore = true)
    @Mapping(target = "requestedBy.contractEndDate", ignore = true)
    @Mapping(target = "requestedBy.contract", ignore = true)
    @Mapping(target = "requestedBy.job", ignore = true)
    @Mapping(target = "requestedBy.createdAt", ignore = true)
    @Mapping(target = "requestedBy.updatedAt", ignore = true)
    StaffingNeed toEntity(StaffingNeedDto dto);
}
