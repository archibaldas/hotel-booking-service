package com.example.hotel_booking_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Entity
@Table(name = "authority", schema = "hotel_booking_schema")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private RoleType authority;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    public GrantedAuthority toAuthority(){
        return new SimpleGrantedAuthority("ROLE_" + authority.name());
    }

    public static UserRole from(RoleType type){
        UserRole userRole = new UserRole();
        userRole.setAuthority(type);
        return userRole;
    }

}
