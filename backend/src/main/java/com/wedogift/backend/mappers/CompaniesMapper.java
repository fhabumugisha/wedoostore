package com.wedogift.backend.mappers;

import com.wedogift.backend.dtos.AddCompanyDto;
import com.wedogift.backend.dtos.DisplayCompanyDto;
import com.wedogift.backend.entities.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UsersMapper.class})
public interface CompaniesMapper {

    DisplayCompanyDto toDto(CompanyEntity companyEntity);

    @Mapping(target = "users", ignore = true)
    CompanyEntity toEntity(AddCompanyDto addCompanyDto);
}
