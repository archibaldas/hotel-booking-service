package com.example.hotel_booking_service.statistics.event;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticEvent {
    @Id
    private String id;
    private String type;
    private Long userId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;

    private LocalDateTime createdAt;
}
