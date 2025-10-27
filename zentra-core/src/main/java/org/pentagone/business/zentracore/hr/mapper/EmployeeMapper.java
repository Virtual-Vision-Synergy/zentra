package org.pentagone.business.zentracore.hr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pentagone.business.zentracore.hr.dto.EmployeeDto;
import org.pentagone.business.zentracore.hr.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "contractId", source = "contract.id")
    EmployeeDto toDto(Employee entity);
}
