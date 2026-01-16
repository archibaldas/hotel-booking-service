package com.example.hotel_booking_service.model.filter;

import com.example.hotel_booking_service.model.validation.RoomFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@RoomFilterValid
public class RoomFilter {
    private Integer pageSize = 10;
    private Integer pageNumber = 0;
    private Long id;
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer countPeople;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Long hotelId;
}
