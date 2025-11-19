package com.looyt.user.management.service.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}