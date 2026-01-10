package com.example.hotel_booking_service.model.service;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;

import java.time.LocalDate;

public interface RoomService extends CRUDService <RoomResponseDto,Room, RoomRequestDto> {
    RoomResponseDto getRoomResponseById(Long id);
    Room bookingDates(Long roomId, LocalDate arrival, LocalDate departure);
    void clearBookingDates(Long roomId, LocalDate arrival, LocalDate departure);
    boolean isExistingPeriod(Long roomId, LocalDate arrival, LocalDate departure);
}
