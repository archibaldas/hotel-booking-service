package com.example.hotel_booking_service.model.validation;

import com.example.hotel_booking_service.model.filter.HotelFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class HotelFilterValidator implements ConstraintValidator<HotelFilterValid, HotelFilter> {
    @Override
    public boolean isValid(HotelFilter value, ConstraintValidatorContext context) {
        return !ObjectUtils.anyNull(value.getPageNumber(), value.getPageSize());
    }
}
