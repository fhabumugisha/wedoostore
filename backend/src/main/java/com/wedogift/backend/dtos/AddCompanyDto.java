package com.wedogift.backend.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddCompanyDto(@NotEmpty String name, @NotEmpty String email, @NotNull Double balance, AddUserDto[] users) {
}
