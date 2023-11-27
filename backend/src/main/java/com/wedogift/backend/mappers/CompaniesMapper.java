package com.wedogift.backend.mappers;

import com.wedogift.backend.dtos.AddCompanyDto;
import com.wedogift.backend.dtos.DisplayCompanyDto;
import com.wedogift.backend.dtos.DisplayUserDto;
import com.wedogift.backend.entities.CompanyEntity;
import org.mapstruct.Mapper;

@Mapper(uses = {UsersMapper.class})
public interface CompaniesMapper {

    DisplayCompanyDto toDto(CompanyEntity companyEntity);

    CompanyEntity toEntity(AddCompanyDto addCompanyDto);
}
