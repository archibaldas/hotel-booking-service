package com.example.hotel_booking_service.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HotelFilterValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HotelFilterValid {

    String message() default "Постраничные поля пусты";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
