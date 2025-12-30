package com.example.hotel_booking_service.web.dto.response;

import lombok.Data;

@Data
public class BookingResponseDto{
  private Long id;
  private UserResponseDto userResponseDto;
  private RoomResponseDto roomResponseDto;
  private LocalDate arrivalDate;
  private LocalDate departureDate;
}
