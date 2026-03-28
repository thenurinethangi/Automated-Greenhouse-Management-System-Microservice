package com.agms.authservice.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.agms.authservice.util.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<APIResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Map<String, Object> errorDetails = baseErrorDetails("Business rule conflict");
        errorDetails.put("error", ex.getMessage());

        APIResponse response = new APIResponse(409, "Conflict", errorDetails);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EmailNotExistsException.class)
    public ResponseEntity<APIResponse> handleEmailNotExistsException(EmailNotExistsException ex) {
        Map<String, Object> errorDetails = baseErrorDetails("Business rule conflict");
        errorDetails.put("error", ex.getMessage());

        APIResponse response = new APIResponse(409, "Register first", errorDetails);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<APIResponse> handleEmailAlreadyExistsException(PasswordIncorrectException ex) {
        Map<String, Object> errorDetails = baseErrorDetails("Business rule conflict");
        errorDetails.put("error", ex.getMessage());

        APIResponse response = new APIResponse(409, "Password incorrect", errorDetails);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<APIResponse> handleDataAccessException(DataAccessException ex) {
        Map<String, Object> errorDetails = baseErrorDetails("Database operation failed");
        errorDetails.put("error",
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());

        APIResponse response = new APIResponse(500, "Database error", errorDetails);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errorDetails = baseErrorDetails("Invalid request data");
        errorDetails.put("error", ex.getMessage());

        APIResponse response = new APIResponse(400, "Bad request", errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleGenericException(Exception ex) {
        Map<String, Object> errorDetails = baseErrorDetails("Unexpected server error");
        errorDetails.put("error", ex.getMessage());

        APIResponse response = new APIResponse(500, "Internal server error", errorDetails);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private Map<String, Object> baseErrorDetails(String reason) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("timestamp", LocalDateTime.now().toString());
        details.put("reason", reason);
        return details;
    }
}
