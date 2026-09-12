package com.hdfclife.desk.config;

import com.hdfclife.desk.exception.ClaimNotFoundException;
import com.hdfclife.desk.exception.DuplicatePolicyException;
import com.hdfclife.desk.exception.InvalidClaimException;
import com.hdfclife.desk.exception.PolicyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    private Map<String, Object> createErrorBody(HttpStatus status, String message) {

        Map<String, Object> body = new HashMap<>();

        body.put("Status", status.value());
        body.put("Message", message);

        return body;
    }

    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClaimNotFound(ClaimNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createErrorBody(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(DuplicatePolicyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicatePolicy(DuplicatePolicyException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(createErrorBody(HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(InvalidClaimException.class)
    public ResponseEntity<Map<String, Object>> handleBa(InvalidClaimException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createErrorBody(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<Map<String, Object>> createErrorHandler(PolicyNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createErrorBody(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
