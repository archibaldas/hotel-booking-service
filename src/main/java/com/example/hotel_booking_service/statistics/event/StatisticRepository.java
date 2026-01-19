package com.example.hotel_booking_service.statistics.event;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticRepository extends MongoRepository<StatisticEvent, String> {
}
