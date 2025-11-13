package com.example.bankcards.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    private String message;
    private LocalDateTime timestamp;
    private String path;
}
