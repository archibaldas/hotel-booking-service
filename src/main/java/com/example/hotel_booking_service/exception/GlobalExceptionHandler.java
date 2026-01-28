package com.example.hotel_booking_service.exception;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.web.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.hotel_booking_service.utils.ErrorResponseGenerator.getErrorResponse;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoFoundEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @LogExecution
    public ErrorResponse handleResourceNotFound(NoFoundEntityException ex) {
        return getErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @LogExecution
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(NotChangeDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @LogExecution
    public ErrorResponse handleChangeUpdatableData(NotChangeDataException ex) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @LogExecution
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(NotAuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @LogExecution
    public ErrorResponse handleUnauthorizedException(NotAuthorizationException ex){
        return getErrorResponse(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @LogExecution
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e){
        return getErrorResponse(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(CsvExportException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @LogExecution
    public ErrorResponse handleCsvExportException(CsvExportException e){
        return getErrorResponse(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @LogExecution
    public ErrorResponse handleGenericException(Exception ex) {
        return getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
}
