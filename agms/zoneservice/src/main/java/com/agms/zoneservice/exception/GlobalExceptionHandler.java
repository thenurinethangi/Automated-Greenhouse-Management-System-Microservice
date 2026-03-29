package com.agms.zoneservice.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.agms.zoneservice.util.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> errorDetails = baseErrorDetails("Invalid request data");
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        errorDetails.put("error", errors);

        APIResponse response = new APIResponse(400, "Bad request", errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<APIResponse> handleWebClientRequestException(WebClientRequestException ex) {
        Map<String, Object> errorDetails = baseErrorDetails("IoT service is unreachable");
        errorDetails.put("error", ex.getMessage());

        APIResponse response = new APIResponse(503, "IoT service unavailable", errorDetails);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<APIResponse> handleWebClientResponseException(WebClientResponseException ex) {
        Map<String, Object> errorDetails = baseErrorDetails("IoT service request failed");
        errorDetails.put("error", ex.getResponseBodyAsString());

        APIResponse response = new APIResponse(ex.getStatusCode().value(), "IoT service error", errorDetails);
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    private Map<String, Object> baseErrorDetails(String reason) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("timestamp", LocalDateTime.now().toString());
        details.put("reason", reason);
        return details;
    }
}
