package com.example.hotel_booking_service.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String status;
    private Object details;
    private String timeStamp;
}
