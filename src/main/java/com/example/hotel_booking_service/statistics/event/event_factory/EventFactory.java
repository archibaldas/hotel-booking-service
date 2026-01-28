package com.example.hotel_booking_service.statistics.event.event_factory;

import com.example.hotel_booking_service.statistics.event.StatisticEvent;

public interface EventFactory<T> {
    StatisticEvent createEvent(T item);
}
