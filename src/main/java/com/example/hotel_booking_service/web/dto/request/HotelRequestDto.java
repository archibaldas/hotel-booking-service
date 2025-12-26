package com.example.hotel_booking_service.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelRequestDto {

    @NotBlank(message = "Название отеля обязательно")
    @Size(min = 2, max = 100, message = "Название отеля должно содержать от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Заголовок объявления обязателен")
    @Size(min = 5, max = 200, message = "Заголовок объявления должен содержать от 5 до 200 символов")
    private String title;

    @NotBlank(message = "Город обязателен")
    @Size(min = 2, max = 100, message = "Название города должно содержать от 2 до 100 символов")
    private String city;

    @NotBlank(message = "Адрес обязателен")
    @Size(min = 5, max = 300, message = "Адрес должен содержать от 5 до 300 символов")
    private String address;

    @NotNull(message = "Расстояние от центра обязательно")
    @PositiveOrZero(message = "Расстояние от центра не может быть отрицательным")
    private Double distanceFromCenter;
}
