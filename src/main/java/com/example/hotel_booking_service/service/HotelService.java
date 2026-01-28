package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.model.entity.Hotel;
import com.example.hotel_booking_service.model.filter.HotelFilter;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.HotelListResponseDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;

public interface HotelService {
    void deleteById(Long id);
    HotelResponseDto update(Long id, HotelRequestDto request);
    Hotel findById(Long id);
    HotelResponseDto create(HotelRequestDto request);
    HotelResponseDto getHotelResponseById(Long id);
    HotelResponseDto updateHotelRating(Long id, int newMark);
    HotelListResponseDto findAllByFilter(HotelFilter filter);
    Long getCount();
}