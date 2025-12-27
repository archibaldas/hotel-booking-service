package com.example.hotel_booking_service.model.service;

import com.example.hotel_booking_service.model.entity.Hotel;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;

public interface HotelService extends CRUDService<HotelResponseDto, Hotel, HotelRequestDto> {
    HotelResponseDto getHotelResponseById(Long id);
}
