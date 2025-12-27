package com.example.hotel_booking_service.web.mapper;

import com.example.hotel_booking_service.model.entity.User;
import com.example.hotel_booking_service.web.dto.request.UserRequestDto;
import com.example.hotel_booking_service.web.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserRequestDto requestDto);
    User updateEntityFromDto(UserRequestDto requestDto, @MappingTarget User user);
    UserResponseDto toResponseDto(User user);
}
