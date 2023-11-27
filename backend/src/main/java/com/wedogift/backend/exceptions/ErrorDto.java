package com.wedogift.backend.exceptions;

import lombok.Builder;

@Builder
public record ErrorDto(String message, int status) {
}
