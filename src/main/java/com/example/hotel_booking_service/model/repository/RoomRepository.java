package com.example.hotel_booking_service.model.repository;

import com.example.hotel_booking_service.model.entity.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    @EntityGraph(attributePaths = "unavailableDates")
    Optional<Room> findById(Long id);
}
