package com.wedogift.backend.controllers;

import com.wedogift.backend.exceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@Slf4j
@RestControllerAdvice()
public class GlobalExeptionHandler {
    @ExceptionHandler({CompanyNonFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorDto> handleFunctionalMessageException(CompanyNonFoundException ex) {
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDto.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND.value()).build() );
    }
    @ExceptionHandler({NotEnoughBalanceException.class})
    public ResponseEntity<ErrorDto> handleFunctionalMessageException(NotEnoughBalanceException ex) {
        return  ResponseEntity.badRequest().body(ErrorDto.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST.value()).build() );
    }


    // Default handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return  ResponseEntity.internalServerError().body(ErrorDto.builder().message(ex.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build() );
    }

    @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<ValidationErrorResponse> onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = ValidationErrorResponse.builder().violations(new ArrayList<>()).build();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.violations().add(
                    Violation.builder()
                            .fieldName(violation.getPropertyPath().toString()).
                            message( violation.getMessage()).build( ));
        }
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse>  onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error =  ValidationErrorResponse.builder().violations(new ArrayList<>()).build();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.violations().add(
                    Violation.builder().fieldName(fieldError.getField()).message(fieldError.getDefaultMessage()).build( ));
        }
        return ResponseEntity.badRequest().body(error);
    }
}
