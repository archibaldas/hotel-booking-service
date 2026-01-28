package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.model.entity.Booking;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService{
    Page<BookingResponseDto> findAllBookingsByPageable(Pageable pageable);
    BookingResponseDto create(BookingRequestDto request);

}
