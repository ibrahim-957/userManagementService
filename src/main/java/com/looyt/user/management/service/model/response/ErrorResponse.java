package com.looyt.user.management.service.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private String errorCode;
    private String path;
    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;

}