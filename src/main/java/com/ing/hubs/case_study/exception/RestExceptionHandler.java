package com.ing.hubs.case_study.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@Order(0)
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = CaseStudyException.class)
    protected ResponseEntity<ErrorResponse> handleCaseStudyException(CaseStudyException ex) {
        log.error("Case Study Exception: {}", ex.getMessage());
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("Validation Exception: {}", ex.getMessage());
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleExceptions(Exception ex) {
        log.error("Exception: {}", ex.getMessage());
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private ErrorResponse buildErrorResponse(String message, HttpStatus status) {
        return ErrorResponse.builder()
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
