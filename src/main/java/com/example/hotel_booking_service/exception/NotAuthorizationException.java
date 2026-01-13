package com.example.hotel_booking_service.exception;

public class NotAuthorizationException extends RuntimeException {
    public NotAuthorizationException(String message) {
        super(message);
    }
}
