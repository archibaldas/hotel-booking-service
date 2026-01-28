package com.example.hotel_booking_service.statistics.event.event_factory.impl;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.model.entity.User;
import com.example.hotel_booking_service.statistics.event.EventType;
import com.example.hotel_booking_service.statistics.event.StatisticEvent;
import com.example.hotel_booking_service.statistics.event.event_factory.EventFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserEventFactory implements EventFactory<User> {
    @Override
    @LogExecution
    public StatisticEvent createEvent(User item) {
        return StatisticEvent.builder()
                .userId(item.getId())
                .type(EventType.USER_REGISTERED)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
