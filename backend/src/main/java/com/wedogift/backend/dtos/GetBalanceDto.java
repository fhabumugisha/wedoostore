package com.wedogift.backend.dtos;

import lombok.Builder;

@Builder
public record GetBalanceDto(Double balance) {
}
