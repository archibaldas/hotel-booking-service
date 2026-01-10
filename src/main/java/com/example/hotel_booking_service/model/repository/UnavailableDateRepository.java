package com.example.hotel_booking_service.model.repository;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.UnavailableDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Long> {

    Boolean existsByRoomAndUnavailableDate(Room room, LocalDate unavailableDate);
}