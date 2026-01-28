package com.example.hotel_booking_service.web.mapper;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.UnavailableDate;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    @Mapping(target = "unavailableDates", ignore = true)
    Room toEntity(RoomRequestDto requestDto);
    @Mapping(target = "unavailableDates", ignore = true)
    Room updateEntity(RoomRequestDto requestDto, @MappingTarget Room room);
    @Mapping(target = "hotelId", source = "hotel.id")
    @Mapping(target = "unavailableDates", qualifiedByName = "extractDatesFromUnavailableDates")
    RoomResponseDto toResponseDto(Room room);

    @Named("extractDatesFromUnavailableDates")
    default List<LocalDate> extractDatesFromUnavailableDates(List<UnavailableDate> unavailableDates){
        if(unavailableDates == null ||unavailableDates.isEmpty()) return Collections.emptyList();
        return unavailableDates.stream()
                .map(UnavailableDate::getUnavailableDate)
                .toList();
    }
}
