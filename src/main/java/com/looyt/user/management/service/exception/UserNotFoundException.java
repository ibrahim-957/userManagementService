package com.looyt.user.management.service.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
    }
    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}