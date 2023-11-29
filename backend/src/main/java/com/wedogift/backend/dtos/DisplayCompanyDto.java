package com.wedogift.backend.dtos;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record DisplayCompanyDto(UUID id, String name, String email, Double balance, List<DisplayUserDto> users) {
}
