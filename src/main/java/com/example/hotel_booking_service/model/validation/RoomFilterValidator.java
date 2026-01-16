package com.example.hotel_booking_service.model.validation;


import com.example.hotel_booking_service.model.filter.RoomFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoomFilterValidator implements ConstraintValidator<RoomFilterValid, RoomFilter> {
    @Override
    public boolean isValid(RoomFilter value, ConstraintValidatorContext context) {

        if(value.getPageNumber() == null || value.getPageSize() == null){
            return false;
        }

        boolean arrival = value.getArrivalDate() != null;
        boolean departure = value.getDepartureDate() != null;

        if(arrival ^ departure) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Дата заезда и выезда должны быть указаны вместе")
                    .addConstraintViolation();
            return false;
        }

        if (arrival && value.getArrivalDate().isAfter(value.getDepartureDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Дата заезда не может быть позже даты выезда"
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
