package com.example.hotel_booking_service.web.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponseDto{
  private Long id;
  private UserResponseDto user;
  private RoomResponseDto room;
  private LocalDate arrivalDate;
  private LocalDate departureDate;
}
