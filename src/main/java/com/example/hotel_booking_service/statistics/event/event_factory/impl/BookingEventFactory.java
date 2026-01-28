package com.example.hotel_booking_service.statistics.event.event_factory.impl;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.model.entity.Booking;
import com.example.hotel_booking_service.statistics.event.EventType;
import com.example.hotel_booking_service.statistics.event.StatisticEvent;
import com.example.hotel_booking_service.statistics.event.event_factory.EventFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class BookingEventFactory implements EventFactory<Booking> {
    @Override
    @LogExecution
    public StatisticEvent createEvent(Booking item) {
        return StatisticEvent.builder()
                .userId(item.getUser().getId())
                .type(EventType.BOOKING_CREATED)
                .createdAt(LocalDateTime.now())
                .arrivalDate(item.getArrivalDate())
                .departureDate(item.getDepartureDate())
                .build();
    }
}
