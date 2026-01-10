package com.example.hotel_booking_service.model.service;

import com.example.hotel_booking_service.model.entity.Booking;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;

public interface BookingService extends CRUDService<BookingResponseDto, Booking, BookingRequestDto> {
}
