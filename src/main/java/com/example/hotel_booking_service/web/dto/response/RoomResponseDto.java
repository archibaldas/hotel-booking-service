package com.example.hotel_booking_service.web.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class RoomResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer number;
    private BigDecimal price;
    private Integer maxPeople;
    private List<LocalDate> unavailableDates;
    private Long hotelId;
}
