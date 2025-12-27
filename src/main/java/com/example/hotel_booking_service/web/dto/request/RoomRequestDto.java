package com.example.hotel_booking_service.web.dto.request;

import com.example.hotel_booking_service.model.entity.Hotel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDto {

    @NotBlank(message = "Название обязательно")
    @Size(max = 100, message = "Название комнаты не более 100 символов")
    private String name;

    @Size(max = 500, message = "Описание комнаты должно быть не более 500 символов")
    private String description;

    @NotNull(message = "Номер комнаты обязателен")
    @Min(value = 1, message = "Номер комнаты должен быть положительным")
    private Integer number;

    @PositiveOrZero(message = "Цена не может быть отрицательной")
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    @Builder.Default
    private Integer maxPeople = 1;

    @Builder.Default
    private List<LocalDate> unavailableDates = new ArrayList<>();

    @NotNull(message = "Значение Id отеля не может быть пустым.")
    @Positive(message = "Id отеля не может быть отрицательным и равным 0")
    private Long hotelId;
}
