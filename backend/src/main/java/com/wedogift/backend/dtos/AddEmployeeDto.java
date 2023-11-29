package com.wedogift.backend.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record AddEmployeeDto(@NotEmpty String name) {
}
