package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.AbstractIntegrationTest;
import com.example.hotel_booking_service.statistics.event.EventType;
import com.example.hotel_booking_service.statistics.event.StatisticEvent;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class StatisticsControllerTest extends AbstractIntegrationTest {

    @Test
    public void whenSendStatisticEvent_thenHandleMessageAndSetToCsvStatistics() {
        long count = statisticRepository.count();

        StatisticEvent userCreated = new StatisticEvent();
        userCreated.setUserId(userId);
        userCreated.setType(EventType.USER_REGISTERED);
        userCreated.setCreatedAt(LocalDateTime.now());
        publisher.send(userCreated);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertThat(statisticRepository.count()).isGreaterThan(count);
                });

    }
}
