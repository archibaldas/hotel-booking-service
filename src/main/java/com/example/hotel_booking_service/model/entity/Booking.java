package com.example.hotel_booking_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking", schema  = "hotel_booking_schema")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "arrival_date")
  private LocalDate arrivalDate;

  @Column(name = "departure_date")
  private LocalDate departureDate;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private User user;
  
}
