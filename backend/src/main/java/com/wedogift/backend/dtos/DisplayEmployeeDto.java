package com.wedogift.backend.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DisplayEmployeeDto(UUID id, String name, Double balance) {
}
