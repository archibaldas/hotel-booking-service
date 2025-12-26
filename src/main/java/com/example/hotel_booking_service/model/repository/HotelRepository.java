package com.example.hotel_booking_service.model.repository;

import com.example.hotel_booking_service.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    boolean existsByNameAndCity(String name, String city);
}
