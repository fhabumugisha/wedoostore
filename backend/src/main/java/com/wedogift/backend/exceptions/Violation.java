package com.wedogift.backend.exceptions;

import lombok.Builder;

@Builder
public record Violation (String fieldName, String message) {




}