package com.example.hotel_booking_service.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelListResponseDto {

    private List<HotelResponseDto> hotels;
    private int pageSize;
    private int pageNumber;
}
