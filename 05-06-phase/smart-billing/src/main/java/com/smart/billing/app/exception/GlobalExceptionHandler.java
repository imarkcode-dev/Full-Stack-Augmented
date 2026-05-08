package com.smart.billing.app.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
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

    /**
     * Handles BadCredentialsException and returns HTTP 401.
     * This exception is thrown when invalid credentials (wrong password) are provided.
     *
     * @param ex the BadCredentialsException thrown
     * @return ResponseEntity with HTTP 401 Unauthorized status and error message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Handles RuntimeException and returns HTTP 500.
     * This is a catch-all handler for unexpected runtime exceptions during execution.
     *
     * @param ex the RuntimeException thrown
     * @return ResponseEntity with HTTP 500 Internal Server Error status and error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    /**
     * Handles HttpMessageNotReadableException and returns HTTP 400.
     * This exception is thrown when the request body contains malformed or invalid JSON.
     *
     * @param ex the HttpMessageNotReadableException thrown
     * @return ResponseEntity with HTTP 400 Bad Request status and error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed JSON request");
    }

}
