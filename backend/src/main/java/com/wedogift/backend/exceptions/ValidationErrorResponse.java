package com.wedogift.backend.exceptions;

import lombok.Builder;

import java.util.List;

@Builder
public record ValidationErrorResponse (List<Violation> violations ) {


}
