package com.example.bankcards.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(req.getRequestURI())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(req.getRequestURI())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(req.getRequestURI())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(req.getRequestURI())
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(req.getRequestURI())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest req
    ) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message("Invalid username or password")
                        .timestamp(LocalDateTime.now())
                        .path(req.getRequestURI())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse("Validation failed");

        return new ResponseEntity<>(
                ApiError.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .path(req.getRequestURI())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }


}
