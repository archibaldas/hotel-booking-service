package com.example.hotel_booking_service.statistics.listner;

import com.example.hotel_booking_service.statistics.event.StatisticEvent;
import com.example.hotel_booking_service.statistics.event.StatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticEventListener {

    private final StatisticRepository repository;

    @KafkaListener(
            topics = "${app.kafka.booking-statistic-topic}",
            groupId = "${app.kafka.groupId}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(StatisticEvent event){
        log.info("Отловлено событие: {}, запись в базу.", event);
        repository.save(event);
    }
}
