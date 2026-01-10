package com.example.hotel_booking_service.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {

  @NotNull(message = "Пользователь обязателен")
  private Long userId;
  @NotNull(message = "Выбор комнаты обязателен")
  private Long roomId;
  @NotNull(message = "Дата вьезда обязательна")
  private LocalDate arrivalDate;
  @NotNull(message = "Дата выезда обязательна")
  private LocalDate departureDate;
}
