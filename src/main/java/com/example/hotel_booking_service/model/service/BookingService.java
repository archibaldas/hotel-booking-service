package com.example.hotel_booking_service.model.service;

import com.example.hotel_booking_service.model.entity.Booking;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;

public interface HotelService extends CRUDService<BookingResponseDto, Booking, BookingRequestDto> {
}
