package com.example.hotel_booking_service.web.mapper;

import com.example.hotel_booking_service.model.entity.Hotel;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {RoomMapper.class})
public interface HotelMapper {

    Hotel toEntity(HotelRequestDto dto);
    Hotel updateEntityFromDto(HotelRequestDto dto, @MappingTarget Hotel hotel);
    HotelResponseDto toResponseDto(Hotel hotel);
}
