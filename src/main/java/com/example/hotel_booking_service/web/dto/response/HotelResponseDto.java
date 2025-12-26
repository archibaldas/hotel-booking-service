package com.example.hotel_booking_service.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDto {

    private Long id;
    private String name;
    private String title;
    private String city;
    private String address;
    private Double distanceFromCenter;
    private Double rating;
    private Integer ratingCount;

}
