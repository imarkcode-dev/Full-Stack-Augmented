package com.smart.billing.app.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.smart.billing.app.dto.ErrorResponse;

/**
 * Global exception handler for the Smart Billing application.
 * 
 * This class is responsible for handling exceptions thrown by controllers
 * across the entire application. It uses Spring's @ControllerAdvice annotation
 * to intercept exceptions and provide consistent error responses.
 * 
 * The handler provides specific treatment for:
 * - ResourceNotFoundException: Returns HTTP 404 with error details
 * - Generic Exception: Returns HTTP 400 with error details
 * 
 * All error responses are formatted using the ErrorResponse DTO containing
 * timestamp, status code, and error message.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns HTTP 404.
     */
   @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                ));
    }

    /**
     * Handles generic exceptions and returns HTTP 400.
     */
     @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                ));
    }

}
