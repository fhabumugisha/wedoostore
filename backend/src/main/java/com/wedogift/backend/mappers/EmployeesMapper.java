package com.wedogift.backend.mappers;

import com.wedogift.backend.dtos.AddEmployeeDto;
import com.wedogift.backend.dtos.DisplayEmployeeDto;
import com.wedogift.backend.entities.EmployeeEntity;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeesMapper {

    DisplayEmployeeDto toDto(EmployeeEntity employeeEntity);

    EmployeeEntity toEntity(AddEmployeeDto addEmployeeDto);
}
