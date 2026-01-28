package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.UnavailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Long> {

    @Query("SELECT CASE WHEN COUNT(ud) > 0 THEN true ELSE false END " +
            "FROM UnavailableDate ud " +
            "WHERE ud.room.id = :roomId " +
            "AND ud.unavailableDate IN :dates")
    Boolean existsByRoomIdAndUnavailableDates(@Param("roomId") Long roomId,
                                              @Param("dates") List<LocalDate> dates);

    Boolean existsByRoomAndUnavailableDate(Room room, LocalDate unavailableDate);

    @Modifying
    @Query("DELETE FROM UnavailableDate ud " +
            "WHERE ud.room.id = :roomId " +
            "AND ud.unavailableDate IN :dates")
    void deleteByRoomIdAndDates(
            @Param("roomId") Long roomId,
            @Param("dates") List<LocalDate> dates
    );
}