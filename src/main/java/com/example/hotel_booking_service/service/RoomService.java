package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.filter.RoomFilter;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.RoomListResponseDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;

import java.time.LocalDate;

public interface RoomService {
    void deleteById(Long id);
    RoomResponseDto update(Long id, RoomRequestDto request);
    RoomResponseDto create(RoomRequestDto request);
    Room findById(Long id);
    RoomResponseDto getRoomResponseById(Long id);
    Room bookingDates(Long roomId, LocalDate arrival, LocalDate departure);
    void clearBookingDates(Long roomId, LocalDate arrival, LocalDate departure);
    boolean isExistingPeriod(Long roomId, LocalDate arrival, LocalDate departure);
    RoomListResponseDto findRoomsByFilter(RoomFilter filter);
    Long getCount();
}
