package com.example.hotel_booking_service.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @NotNull(messagee = "Дата выезда обязательна")
  private LocalDate departureDate;
}
