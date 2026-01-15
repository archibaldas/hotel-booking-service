package com.example.hotel_booking_service.model.filter;

import com.example.hotel_booking_service.model.validation.HotelFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@HotelFilterValid
public class HotelFilter {
    private Integer pageSize = 10;
    private Integer pageNumber = 0;
    private Long id;
    private String name;
    private String title;
    private String city;
    private String address;
    private Double MinDistanceFromCenter;
    private Double MaxDistanceFromCenter;
    private Double minRating;
    private Double maxRating;
    private Integer minRatingCount;
    private Integer maxRatingCount;
}
