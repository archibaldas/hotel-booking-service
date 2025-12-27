package com.example.hotel_booking_service.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Min(value = 3, message = "Длинна имени пользователя не может быть меньше 3-ех знаков")
    @Max(value = 50, message = "Длинна имени пользователя не может превышать 50 знаков")
    private String username;
    @NotBlank(message = "Пароль не может быть пустым")
    @Min(value = 6, message = "Длинна пароля не может быть меньше 6 знаков")
    @Max(value = 50, message = "Длинна пароля не может превышать 50 знаков")
    private String password;
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Email(message = "Не верный формат электронной почты")
    private String email;
}
