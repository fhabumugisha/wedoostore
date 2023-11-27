package com.wedogift.backend.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record AddUserDto(@NotEmpty String name) {
}
