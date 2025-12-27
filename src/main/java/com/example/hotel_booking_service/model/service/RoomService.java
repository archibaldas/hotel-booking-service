package com.example.hotel_booking_service.model.service;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;

public interface RoomService extends CRUDService <RoomResponseDto,Room, RoomRequestDto> {
    RoomResponseDto getRoomResponseById(Long id);
}
