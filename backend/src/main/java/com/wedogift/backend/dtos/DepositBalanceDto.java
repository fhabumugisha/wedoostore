package com.wedogift.backend.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DepositBalanceDto(@NotNull LocalDate depositDate, @NotNull Double balance, @NotNull  EnumDepositType enumDepositType) {
}
