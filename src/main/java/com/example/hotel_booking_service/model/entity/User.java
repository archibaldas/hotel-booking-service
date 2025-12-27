package com.example.hotel_booking_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", schema = "hotel_booking_schema")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    @Column(name = "password", nullable = false, length = 50)
    private String password;
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;
    @Column(name = "role_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType;
}
