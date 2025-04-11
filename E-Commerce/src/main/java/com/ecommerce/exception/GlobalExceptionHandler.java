package com.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistException(ResourceAlreadyExistException ex) {
        HttpStatus statusCode = HttpStatus.CONFLICT;

        return new ResponseEntity<>(
                new ErrorResponse(statusCode.value(), ex.getMessage()),
                statusCode
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        HttpStatus statusCode = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(
                new ErrorResponse(statusCode.value(), ex.getMessage()),
                statusCode
        );
    }

    @ExceptionHandler(NullResourceException.class)
    public ResponseEntity<ErrorResponse> handleNullResourceException(NullResourceException ex) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                new ErrorResponse(statusCode.value(), ex.getMessage()),
                statusCode
        );
    }

    @ExceptionHandler(InvalidInputResourceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputResourceException(InvalidInputResourceException ex) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                new ErrorResponse(statusCode.value(), ex.getMessage()),
                statusCode
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                new ErrorResponse(statusCode.value(), ex.getMessage()),
                statusCode
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
