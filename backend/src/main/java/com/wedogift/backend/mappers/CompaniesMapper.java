package com.wedogift.backend.mappers;

import com.wedogift.backend.dtos.AddCompanyDto;
import com.wedogift.backend.dtos.DisplayCompanyDto;
import com.wedogift.backend.entities.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {EmployeesMapper.class})
public interface CompaniesMapper {

    DisplayCompanyDto toDto(CompanyEntity companyEntity);

    @Mapping(target = "employees", ignore = true)
    CompanyEntity toEntity(AddCompanyDto addCompanyDto);
}
