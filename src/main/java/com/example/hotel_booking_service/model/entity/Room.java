package com.example.hotel_booking_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms", schema = "hotel_booking_schema",uniqueConstraints = @UniqueConstraint(
        name = "uq_room_hotel_number",
        columnNames = {"hotel_id", "number"}
))
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "number")
    private Integer number;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "max_people")
    private Integer maxPeople;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UnavailableDate> unavailableDates = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false, updatable = false)
    private Hotel hotel;
}
