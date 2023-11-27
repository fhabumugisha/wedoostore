package com.wedogift.backend.mappers;

import com.wedogift.backend.dtos.AddUserDto;
import com.wedogift.backend.dtos.DisplayUserDto;
import com.wedogift.backend.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UsersMapper {

    DisplayUserDto toDto(UserEntity userEntity);

    UserEntity toEntity(AddUserDto addUserDto);
}
