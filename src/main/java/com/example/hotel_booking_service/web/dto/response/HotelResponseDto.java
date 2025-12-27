package com.example.hotel_booking_service.web.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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
    private List<RoomResponseDto> rooms;

}
