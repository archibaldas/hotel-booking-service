package com.example.hotel_booking_service.utils;

import com.example.hotel_booking_service.exception.NotChangeDataException;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class DateUtils {
    public static List<LocalDate> getDateListBetweenArrivalAndDepartureDates(LocalDate arrival, LocalDate departure){
        long daysBetween = ChronoUnit.DAYS.between(arrival,departure);
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i <= daysBetween; i++){
            LocalDate date = arrival.plusDays(i);
            dateList.add(date);
        }
        return dateList;
    }
}
