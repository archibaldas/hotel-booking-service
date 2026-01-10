package com.example.hotel_booking_service.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels", schema = "hotel_booking_schema")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "address", nullable = false, length = 300)
    private String address;

    @Column(name = "distance_from_center", nullable = false)
    private Double distanceFromCenter;

    @Column(name = "rating")
    @Range(min = 1, max = 5, message = "Значение рейтинга не может быть меньше 1 и больше 5")
    @Builder.Default
    private Double rating = 1.0;

    @Column(name = "rating_count", nullable = false)
    @Builder.Default
    private Integer ratingCount = 0;

    @OneToMany(mappedBy = "hotel", orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
}
