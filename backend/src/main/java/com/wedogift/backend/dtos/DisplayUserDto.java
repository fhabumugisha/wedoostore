package com.wedogift.backend.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DisplayUserDto(UUID id, String name, Double balance) {
}
