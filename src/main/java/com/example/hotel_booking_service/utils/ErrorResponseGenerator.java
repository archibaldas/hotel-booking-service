package com.example.hotel_booking_service.utils;

import com.example.hotel_booking_service.web.dto.response.ErrorResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.example.hotel_booking_service.utils.HttpStatusCodeRusMessenger.getMessageByStatus;

@UtilityClass
public class ErrorResponseGenerator {
    public static ErrorResponse getErrorResponse(HttpStatus httpStatus, Exception ex){
        String message = getMessageByStatus(httpStatus);
        String status = httpStatus.toString();
        Object details;
        if(ex instanceof MethodArgumentNotValidException){
            Map<String, String> errorMap = new HashMap<>();
            for (ObjectError error : ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors()) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errorMap.put(fieldName, errorMessage);
            }
            details = errorMap;
        } else {
            details = ex.getMessage();
        }
        String timeStamp = Instant.now().toString();
        return new ErrorResponse(message, status, details, timeStamp);
    }
}
