package com.example.hotel_booking_service.model.service;

import com.example.hotel_booking_service.model.entity.RoleType;
import com.example.hotel_booking_service.model.entity.User;
import com.example.hotel_booking_service.web.dto.request.UserRequestDto;
import com.example.hotel_booking_service.web.dto.response.UserResponseDto;

public interface UserService extends CRUDService<UserResponseDto, User, UserRequestDto> {

    UserResponseDto getUserById(Long id);
    UserResponseDto createNewUser(UserRequestDto requestDto, RoleType roleType);
    User findByUsername(String name);
    UserResponseDto getUserByUsername(String name);


}
