package com.example.hotel_booking_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "unavailable_dates", schema = "hotel_booking_schema", uniqueConstraints = @UniqueConstraint(
        name = "uq_room_id_unavailable_date",
        columnNames = {"room_id", "unavailable_date"}
        ))
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UnavailableDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unavailable_date")
    private LocalDate unavailableDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
