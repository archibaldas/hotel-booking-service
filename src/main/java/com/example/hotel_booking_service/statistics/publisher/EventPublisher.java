package com.example.hotel_booking_service.statistics.publisher;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.statistics.event.StatisticEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    @Value("${app.kafka.booking-statistic-topic}")
    private String statisticTopic;
    private final KafkaTemplate<String, StatisticEvent> kafkaTemplate;

    @LogExecution
    public void send(StatisticEvent event){
        kafkaTemplate.send(statisticTopic, event);
    }
}
