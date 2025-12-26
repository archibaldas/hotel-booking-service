package com.example.hotel_booking_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoFoundEntityException extends RuntimeException {
    public NoFoundEntityException(String message) {
        super(message);
    }
}
