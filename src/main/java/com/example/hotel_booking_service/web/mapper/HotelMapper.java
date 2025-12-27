package com.example.hotel_booking_service.web.mapper;

import com.example.hotel_booking_service.model.entity.Hotel;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    Hotel toEntity(HotelRequestDto dto);
    Hotel updateEntityFromDto(HotelRequestDto dto, @MappingTarget Hotel hotel);
    @Mapping(target = "rooms", ignore = true)
    HotelResponseDto toResponseDto(Hotel hotel);
}
