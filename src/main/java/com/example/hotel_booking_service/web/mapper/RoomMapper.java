package com.example.hotel_booking_service.web.mapper;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    @Mapping(target = "unavailableDates", ignore = true)
    Room toEntity(RoomRequestDto requestDto);
    @Mapping(target = "unavailableDates", ignore = true)
    Room updateEntity(RoomRequestDto requestDto, @MappingTarget Room room);
    @Mapping(target = "hotelId", source = "hotel.id")
    @Mapping(target = "unavailableDates", ignore = true)
    RoomResponseDto toResponseDto(Room room);
}
