package com.agms.cropInventoryService.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.agms.cropInventoryService.util.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.warn("Validation error occurred: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = baseErrorDetails("Invalid request data");
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        errorDetails.put("error", errors);

        APIResponse response = new APIResponse(400, "Validation failed", errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<APIResponse> handleDataAccessException(DataAccessException ex) {
        logger.error("Database operation failed: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorDetails = baseErrorDetails("Database operation failed");
        errorDetails.put("error",
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());

        APIResponse response = new APIResponse(500, "Database error", errorDetails);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = baseErrorDetails("Resource Not Found");
        errorDetails.put("error", ex.getMessage());

        APIResponse response = new APIResponse(404, "Resource Not Found", errorDetails);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = baseErrorDetails("Invalid request data");
        errorDetails.put("error", ex.getMessage());

        APIResponse response = new APIResponse(400, "Bad request", errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected server error: {}", ex.getMessage(), ex);
        
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
