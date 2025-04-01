package com.example.ShipManagementSystem.exception;

import com.example.ShipManagementSystem.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handle Resource Not Found Exception (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse<>(404, ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    // Handle Database Integrity Violation (e.g., Duplicate Key, Foreign Key)
    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ApiResponse<String>> handleDatabaseConstraintViolation(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(400, "Database Constraint Violation: " + ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    // Handle Empty Result when trying to delete/update a non-existing record
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleEmptyResult(EmptyResultDataAccessException ex) {
        return new ResponseEntity<>(new ApiResponse<>(404, "No record found to delete/update", null), HttpStatus.NOT_FOUND);
    }

    // Handle Other General Exceptions (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(500, "Internal Server Error: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
