package com.example.hotel_booking_service.model.repository;

import com.example.hotel_booking_service.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
